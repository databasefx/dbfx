package cn.navigational.dbfx.controls.tree;

import cn.navigational.dbfx.SQLClientManager;
import cn.navigational.dbfx.controller.NavigatorToolBarController;
import cn.navigational.dbfx.controls.tree.cell.NTreeCell;
import cn.navigational.dbfx.model.DbInfo;
import cn.navigational.dbfx.utils.AlertUtils;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class CustomTreeView extends TreeView<String> implements NavigatorToolBarController.NavigatorToolBarHandler {
    private static CustomTreeView navigator;
    private static final String DEFAULT_STYLE_CLASS = "l-navigator";

    private final NavigatorToolBarController toolBarController;


    private CustomTreeView() {
        this.setShowRoot(false);
        this.setCellFactory(it -> new NTreeCell());
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.toolBarController = new NavigatorToolBarController(this);
        SQLClientManager.Companion.getDbInfo().forEach(this::createClientTree);
        SQLClientManager.Companion.getDbInfo().addListener((ListChangeListener<DbInfo>) it -> {
            while (it.next()) {
                //Listener add operation
                if (it.wasAdded()) {
                    it.getAddedSubList().forEach(this::createClientTree);
                }
                //Listener remove operation
                if (it.wasRemoved()) {
                    it.getRemoved().forEach(this::deleteClientTree);
                }
            }
        });
        this.getSelectionModel().getSelectedItems().addListener((ListChangeListener<TreeItem<String>>) c -> {
            var size = c.getList().size();
            if (size <= 0) {
                return;
            }
            var selectItem = c.getList().get(0);
            this.toolBarController.updateSelect((AbstractBaseTreeItem.InnerTreeItem) selectItem);
        });
    }

    /**
     * Create a new client tree
     *
     * @param it Database info
     */
    private void createClientTree(DbInfo it) {
        var item = new DatabaseTreeItem(it);
        item.setText(it.getName());
        if (this.getRoot() == null) {
            this.setRoot(new TreeItem<>());
        }
        this.getRoot().getChildren().add(item.getTreeItem());
    }

    /**
     * Delete a client tree
     *
     * @param it Database info
     */
    private void deleteClientTree(DbInfo it) {
        for (var child : getRoot().getChildren()) {
            var item = (DatabaseTreeItem) ((AbstractBaseTreeItem.InnerTreeItem) child).getControl();
            if (it.getUuid().equals(item.getUuId())) {
                item.delConnect();
                break;
            }
        }
    }

    public void updateConnection(DbInfo dbInfo) {
        var optional = getRoot().getChildren().stream()
                .map(it -> (AbstractBaseTreeItem.InnerTreeItem) it)
                .map(it -> (DatabaseTreeItem) it.getControl())
                .filter(it -> it.getUuId().equals(dbInfo.getUuid()))
                .findAny();
        if (optional.isEmpty()) {
            return;
        }
        var item = optional.get();
        item.update(dbInfo);
        if (!item.conStatus()) {
            return;
        }
        var result = AlertUtils.showSimpleConfirmDialog("当前连接已改变,是否重新连接?");
        if (!result) {
            return;
        }
        //Start reconnection
        item.reConnect();
    }

    @Override
    public void onAction(NavigatorToolBarController.ToolBarAction action, AbstractBaseTreeItem control) {
        if (control == null) {
            return;
        }
        switch (action) {
            case FLUSH -> control.onAction(TreeItemMenuHandler.MenuAction.FLUSH);
            case STOP -> control.onAction(TreeItemMenuHandler.MenuAction.DISCOUNT_CONNECT);
            case TERMINAL -> control.onAction(TreeItemMenuHandler.MenuAction.OPEN_TERMINAL);
            case DB_CONFIG -> control.onAction(TreeItemMenuHandler.MenuAction.EDIT_CONNECT);
            default -> {

            }
        }
    }

    public ToolBar getControlBar() {
        return this.toolBarController.getParent();
    }

    public static synchronized CustomTreeView getNavigator() {
        if (navigator == null) {
            navigator = new CustomTreeView();
        }
        return navigator;
    }
}

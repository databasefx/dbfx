package cn.navigational.dbfx.controls.tree;

import cn.navigational.dbfx.BaseTreeItem;
import cn.navigational.dbfx.SQLClientManager;
import cn.navigational.dbfx.i18n.I18N;
import cn.navigational.dbfx.kit.utils.StringUtils;
import cn.navigational.dbfx.model.SQLClient;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Custom Tree Item
 *
 * @author yangkui
 * @since 1.0
 */
public class AbstractBaseTreeItem {
    /**
     * Show text node
     */
    private final Label text;
    /**
     * Prefix node
     */
    private final Label prefix;
    /**
     * Suffix node
     */
    private final Label suffix;
    /**
     * Inner {@link TreeItem} object
     */
    private final InnerTreeItem treeItem;
    /**
     * Current {@link TreeItem} support menu list
     */
    private final List<MenuItem> menuItemList;

    private final TreeItemContextMenu contextMenu;

    private final Logger logger;

    /****************************************************************************
     *                              default css class name                       *
     ****************************************************************************/
    private static final String TEXT_DEFAULT_STYLE_CLASS = "tree-item-text";
    private static final String SUFFIX_DEFAULT_STYLE_CLASS = "tree-item-suffix";
    private static final String PREFIX_DEFAULT_STYLE_CLASS = "tree-item-prefix";
    private static final String GRAPHIC_DEFAULT_STYLE_CLASS = "tree-item-graphic";
    private static final String CONTENT_DEFAULT_STYLE_CLASS = "tree-item-content";
    protected static final String DATA_INDICATOR_STYLE_CLASS = "tree-item-data-indicator";

    private final ChangeListener<String> suffixTextListener;
    protected final ListChangeListener<TreeItem<String>> listListener;


    public AbstractBaseTreeItem() {
        this("");
    }

    public AbstractBaseTreeItem(String text) {

        var content = new HBox();
        var graphics = new HBox();
        this.suffix = new Label();
        this.prefix = new Label();
        this.text = new Label(text);
        this.menuItemList = new ArrayList<>();
        this.contextMenu = new TreeItemContextMenu();
        content.getChildren().add(this.text);
        graphics.getChildren().addAll(prefix, content, suffix);
        graphics.setOnMouseClicked(this::onMouseClicked);
        graphics.setOnContextMenuRequested(event ->
                this.contextMenu.show(graphics, event.getScreenX(), event.getScreenY()));
        //register css class
        this.text.getStyleClass().add(TEXT_DEFAULT_STYLE_CLASS);
        content.getStyleClass().add(CONTENT_DEFAULT_STYLE_CLASS);
        graphics.getStyleClass().add(GRAPHIC_DEFAULT_STYLE_CLASS);
        this.suffix.getStyleClass().add(SUFFIX_DEFAULT_STYLE_CLASS);
        this.prefix.getStyleClass().add(PREFIX_DEFAULT_STYLE_CLASS);

        this.treeItem = new InnerTreeItem(this, graphics);
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.suffixTextListener = (observable, oldValue, newValue) -> {
            if (StringUtils.isEmpty(newValue)) {
                this.suffix.getStyleClass().remove(DATA_INDICATOR_STYLE_CLASS);
            } else {
                if (this.suffix.getStyleClass().contains(DATA_INDICATOR_STYLE_CLASS)) {
                    return;
                }
                this.suffix.getStyleClass().add(DATA_INDICATOR_STYLE_CLASS);
            }
        };
        this.listListener = c -> {
            var size = treeItem.getChildren().size();
            Platform.runLater(() -> this.suffix.setText(String.valueOf(size == 0 ? "" : size)));
        };
        this.suffix.textProperty().addListener(this.suffixTextListener);
    }


    protected void addChildren(AbstractBaseTreeItem... items) {
        this.addChildren(List.of(items));
    }

    protected void addChildren(List<AbstractBaseTreeItem> items) {
        var children = items
                .stream().map(AbstractBaseTreeItem::getTreeItem).collect(Collectors.toList());
        Platform.runLater(() -> treeItem.getChildren().addAll(children));
    }

    protected void reListListener() {
        this.treeItem.getChildren().addListener(this.listListener);
    }

    /**
     * Clear current {@link TreeItem} children
     */
    protected void clear() {
        this.treeItem.getChildren().forEach(this::exeDispose);
        this.treeItem.getChildren().clear();
    }

    /**
     * Delete child nodes in batch
     *
     * @param items Node list
     */
    protected void removeItem(TreeItem<String>... items) {
        for (TreeItem<String> treeItem : items) {
            this.exeDispose(treeItem);
        }
        getTreeItem().getChildren().removeAll(items);
    }

    /**
     * Remove single child node
     *
     * @param item Target item
     */
    protected void removeItem(TreeItem<String> item) {
        exeDispose(item);
        item.getParent().getChildren().remove(item);
    }

    private void exeDispose(TreeItem<String> item) {
        if (item instanceof InnerTreeItem) {
            ((InnerTreeItem) item).control.dispose();
            //Recursively release subordinate resources
            for (TreeItem<String> cItem : item.getChildren()) {
                exeDispose(cItem);
            }
        }
    }

    /**
     * Current {@link TreeItem} has remove callback that method
     */
    public void dispose() {
        this.treeItem.getChildren().removeListener(this.listListener);
        this.suffix.textProperty().removeListener(this.suffixTextListener);
    }

    /**
     * Mouse event fire
     *
     * @param event Mouse event source
     */
    protected void onMouseClicked(MouseEvent event) {
    }

    /**
     * Flush data
     *
     * @param event {@link ActionEvent}
     */
    protected void flush(ActionEvent event) {

    }

    /**
     * Open connection
     *
     * @param event {@link ActionEvent}
     */
    protected void connect(ActionEvent event) {

    }

    /**
     * Discount connection
     *
     * @param event
     */
    protected void discount(ActionEvent event) {

    }

    /**
     * Edit connection
     *
     * @param event
     */
    protected void editConnect(ActionEvent event) {

    }

    /**
     * Delete connection
     *
     * @param event
     */
    protected void delConnection(ActionEvent event) {

    }

    /**
     * Context menu action list
     */
    protected enum ContextMenuAction {
        /**
         * Add {@link MenuItem} to {@link ContextMenu}
         */
        ADD,
        /**
         * Remove {@link MenuItem} from {@link ContextMenu}
         */
        REMOVE,
        /**
         * Enable {@link MenuItem}
         */
        ENABLE,
        /**
         * Disable {@link MenuItem}
         */
        DISABLE,
    }

    /**
     * {@link javafx.scene.control.TreeItem} current support menu
     */
    public enum MenuAction {
        /**
         * FLUSH
         */
        FLUSH,
        /**
         * Edit connect
         */
        EDIT_CONNECT,
        /**
         * Open connection
         */
        OPEN_CONNECT,
        /**
         * Delete a connect
         */
        DELETE_CONNECT,
        /**
         * Discount connect
         */
        DISCOUNT_CONNECT
    }

    public class TreeItemContextMenu extends ContextMenu {
        private final MenuItem flush = new MenuItem(I18N.getString("label.flush"));
        private final MenuItem discount = new MenuItem(I18N.getString("navigation.menu.discount"));
        private final MenuItem connection = new MenuItem(I18N.getString("navigation.menu.open"));
        private final MenuItem editConnect = new MenuItem(I18N.getString("navigation.menu.edit.connection"));
        private final MenuItem delConnect = new MenuItem(I18N.getString("navigation.menu.remove.connection"));

        public TreeItemContextMenu() {
            this.flush.setOnAction(AbstractBaseTreeItem.this::flush);
            this.discount.setOnAction(AbstractBaseTreeItem.this::discount);
            this.connection.setOnAction(AbstractBaseTreeItem.this::connect);
            this.editConnect.setOnAction(AbstractBaseTreeItem.this::editConnect);
            this.delConnect.setOnAction(AbstractBaseTreeItem.this::delConnection);
        }

        public void updateItem(ContextMenuAction action, MenuAction... targets) {
            for (MenuAction target : targets) {
                this.updateItem(action, target);
            }
        }

        public void updateItem(ContextMenuAction action, MenuAction target) {
            switch (action) {
                case ADD -> addMenuItem(target);
                case DISABLE -> getItem(target).setDisable(true);
                case REMOVE -> getItems().remove(getItem(target));
                case ENABLE -> getItem(target).setDisable(false);
            }
        }

        private void addMenuItem(MenuAction target) {
            var item = getItem(target);
            if (!getItems().contains(item)) {
                getItems().add(item);
            }
        }

        private MenuItem getItem(MenuAction target) {
            return switch (target) {
                case FLUSH -> flush;
                case OPEN_CONNECT -> connection;
                case DISCOUNT_CONNECT -> discount;
                case EDIT_CONNECT -> editConnect;
                case DELETE_CONNECT -> delConnect;
            };
        }
    }

    /**
     * Get sql client from {@link SQLClientManager}
     *
     * @param uuid Sql client uuid
     * @return If current sql client exist return SQLClient otherwise return empty
     */
    protected Optional<SQLClient> getSqlClient(String uuid) {
        var client = SQLClientManager.Companion.getManager().getClient(uuid);
        final Optional<SQLClient> optional;
        if (client == null) {
            optional = Optional.empty();
        } else {
            optional = Optional.of(client);
        }
        return optional;
    }

    public static class InnerTreeItem extends TreeItem<String> {
        private final AbstractBaseTreeItem control;

        public InnerTreeItem(AbstractBaseTreeItem treeItem, Node gra) {
            this.control = treeItem;
            this.setGraphic(gra);
        }

        public AbstractBaseTreeItem getControl() {
            return control;
        }
    }

    /**
     * Gets the full path of the current {@link TreeItem}
     *
     * @return Full path
     */
    public String getFullPath() {
        var tabPath = new StringBuilder();
        var parent = this.treeItem;
        while (true) {
            var control = parent.control;
            var subPath = control.getText();
            if (control instanceof DatabaseTreeItem) {
                subPath = ((DatabaseTreeItem) control).getUuId();
            }
            tabPath.insert(0, "\\" + subPath);
            var temp = parent.getParent();
            //If current item is root item,stop loop
            if (temp == null || temp.getParent() == null) {
                tabPath = new StringBuilder(tabPath.substring(1));
                break;
            }
            parent = (InnerTreeItem) temp;
        }
        if (StringUtils.isEmpty(tabPath.toString())) {
            logger.error("Not found every path for [{}]", this);
            throw new RuntimeException("Not found every path for [" + this + "]");
        }
        return tabPath.toString();
    }

    public void setPrefixTx(String tx) {
        this.prefix.setText(tx);
    }

    public void setSuffixTx(String tx) {
        this.suffix.setText(tx);
    }

    public void setPrefixGra(Node gra) {
        this.prefix.setGraphic(gra);
    }

    public Node getPrefixGra() {
        return this.prefix.getGraphic();
    }

    public void setSuffixGra(Node gra) {
        this.suffix.setGraphic(gra);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public String getText() {
        return text.getText();
    }

    public InnerTreeItem getTreeItem() {
        return treeItem;
    }

    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }

    public TreeItemContextMenu getContextMenu() {
        return contextMenu;
    }

    public Label getPrefix() {
        return prefix;
    }

    public Label getSuffix() {
        return suffix;
    }
}

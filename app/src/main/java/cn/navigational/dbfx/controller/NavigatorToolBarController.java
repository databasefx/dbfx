package cn.navigational.dbfx.controller;

import cn.navigational.dbfx.AbstractFxmlController;
import cn.navigational.dbfx.DatabaseMetaManager;
import cn.navigational.dbfx.SQLClientManager;
import cn.navigational.dbfx.controls.tree.AbstractBaseTreeItem;
import cn.navigational.dbfx.controls.tree.CustomTreeView;
import cn.navigational.dbfx.controls.tree.DatabaseTreeItem;
import cn.navigational.dbfx.controls.tree.TreeItemMenuHandler;
import cn.navigational.dbfx.kit.enums.Clients;
import cn.navigational.dbfx.model.DatabaseMeta;
import cn.navigational.dbfx.model.SQLClient;
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder;
import cn.navigational.dbfx.view.CreateConViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;

import static cn.navigational.dbfx.config.AppConstantsKt.*;
import static cn.navigational.dbfx.config.ControllerPathKt.N_TOP_BAR;

/**
 * Navigator tool bar controller
 *
 * @author yangkui
 * @since 1.0
 */
public class NavigatorToolBarController extends AbstractFxmlController<ToolBar> {

    @FXML
    private Button flush;

    @FXML
    private Button dbConfig;

    @FXML
    private Button duplicate;

    @FXML
    private Button stop;

    @FXML
    private Button terminal;

    @FXML
    private MenuButton dbList;


    public NavigatorToolBarController(CustomTreeView treeView) {
        super(N_TOP_BAR);
        this.stop.setGraphic(SvgImageTranscoder.svgToImageView(STOP_X16));
        this.dbList.setGraphic(SvgImageTranscoder.svgToImageView(ADD_X16));
        this.flush.setGraphic(SvgImageTranscoder.svgToImageView(FLUSH_X16));
        this.terminal.setGraphic(SvgImageTranscoder.svgToImageView(TERMINAL_X16));
        this.dbConfig.setGraphic(SvgImageTranscoder.svgToImageView(DB_CONFIG_X16));
        this.duplicate.setGraphic(SvgImageTranscoder.svgToImageView(DUPLICATE_X16));
        //register event
        this.stop.setOnAction(event -> treeView.onAction(ToolBarAction.STOP, this.control));
        this.flush.setOnAction(event -> treeView.onAction(ToolBarAction.FLUSH, this.control));
        this.terminal.setOnAction(event -> treeView.onAction(ToolBarAction.TERMINAL, this.control));
        this.dbConfig.setOnAction(event -> treeView.onAction(ToolBarAction.DB_CONFIG, this.control));
        this.duplicate.setOnAction(event -> treeView.onAction(ToolBarAction.DUPLICATE, this.control));
        for (DatabaseMeta meta : DatabaseMetaManager.Companion.getMetas()) {
            var name = meta.getName();
            var cl = Clients.getClient(name);
            var gra = SQLClient.getMiniIcon(cl);
            var item = new MenuItem(name, SvgImageTranscoder.svgToImageView(gra));
            item.setUserData(cl);
            item.setOnAction(this::createConnect);
            this.dbList.getItems().add(item);
        }

    }

    private AbstractBaseTreeItem control;

    public void updateSelect(AbstractBaseTreeItem.InnerTreeItem treeItem) {
        if (control == null) {
            this.duplicate.setDisable(false);
        }
        this.control = treeItem.getControl();
        if (control instanceof DatabaseTreeItem) {
            this.stop.setDisable(!((DatabaseTreeItem) control).conStatus());
        } else {
            this.stop.setDisable(true);
        }
        var actions = control.getMenuAction();
        this.flush.setDisable(!actions.contains(TreeItemMenuHandler.MenuAction.FLUSH));
        this.dbConfig.setDisable(!actions.contains(TreeItemMenuHandler.MenuAction.EDIT_CONNECT));
        this.terminal.setDisable(!actions.contains(TreeItemMenuHandler.MenuAction.OPEN_TERMINAL));
    }

    private void createConnect(ActionEvent event) {
        var item = (MenuItem) event.getSource();
        var cl = (Clients) item.getUserData();
        new CreateConViewController(cl).showStage();
    }

    /**
     * Navigator tool bar handler
     */
    public interface NavigatorToolBarHandler {
        /**
         * When tool bar any action will trigger callback
         *
         * @param action  {@link ToolBarAction}
         * @param control current select {@link javafx.scene.control.TreeItem}
         */
        void onAction(ToolBarAction action, AbstractBaseTreeItem control);
    }


    public enum ToolBarAction {
        /**
         * FLUSH
         */
        FLUSH,
        /**
         * Database config
         */
        DB_CONFIG,
        /**
         * Copy current database config
         */
        DUPLICATE,
        /**
         * Stop current database
         */
        STOP,
        /**
         * Open Sql terminal
         */
        TERMINAL
    }
}

package cn.navigational.dbfx.controller;

import cn.navigational.dbfx.AbstractFxmlController;
import cn.navigational.dbfx.controls.tree.CustomTreeView;
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
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
    /**
     * Navigator tool bar handler
     */
    public interface NavigatorToolBarHandler {
        /**
         * When tool bar any action will trigger callback
         *
         * @param action {@link ToolBarAction}
         */
        void onAction(ToolBarAction action);
    }

    @FXML
    private MenuButton dbList;

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

    public NavigatorToolBarController(CustomTreeView treeView) {
        super(N_TOP_BAR);
        this.stop.setGraphic(SvgImageTranscoder.svgToImageView(STOP_X16));
        this.dbList.setGraphic(SvgImageTranscoder.svgToImageView(ADD_X16));
        this.flush.setGraphic(SvgImageTranscoder.svgToImageView(FLUSH_X16));
        this.terminal.setGraphic(SvgImageTranscoder.svgToImageView(TERMINAL_X16));
        this.dbConfig.setGraphic(SvgImageTranscoder.svgToImageView(DB_CONFIG_X16));
        this.duplicate.setGraphic(SvgImageTranscoder.svgToImageView(DUPLICATE_X16));
        //register event
        this.stop.setOnAction(event -> treeView.onAction(ToolBarAction.STOP));
        this.flush.setOnAction(event -> treeView.onAction(ToolBarAction.FLUSH));
        this.terminal.setOnAction(event -> treeView.onAction(ToolBarAction.TERMINAL));
        this.dbConfig.setOnAction(event -> treeView.onAction(ToolBarAction.DB_CONFIG));
        this.duplicate.setOnAction(event -> treeView.onAction(ToolBarAction.DUPLICATE));
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

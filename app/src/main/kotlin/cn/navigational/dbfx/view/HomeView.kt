package cn.navigational.dbfx.view

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.View
import cn.navigational.dbfx.config.HOME_PAGE
import cn.navigational.dbfx.config.N_EVENT_LOG
import cn.navigational.dbfx.config.N_TERMINAL
import cn.navigational.dbfx.controller.BottomNavigationExpandPaneController
import cn.navigational.dbfx.controller.LogController
import cn.navigational.dbfx.controller.TerminalController
import cn.navigational.dbfx.controls.tab.SQLTerminalTab
import cn.navigational.dbfx.handler.MainTabPaneHandler
import cn.navigational.dbfx.controls.tree.CustomTreeView
import cn.navigational.dbfx.handler.closeAppOccurResource
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.utils.AlertUtils
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.SplitPane
import javafx.stage.WindowEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeView : View<Void>(HOME_PAGE) {
    @FXML
    private lateinit var terminal: Button

    @FXML
    private lateinit var eventLog: Button

    @FXML
    private lateinit var mSPane: SplitPane

    @FXML
    private lateinit var splitPane: SplitPane

    private lateinit var navigator: CustomTreeView

    private lateinit var tabHandler: MainTabPaneHandler

    private lateinit var expandPaneController: BottomNavigationExpandPaneController


    override fun onCreated(scene: Scene?) {
        this.title = "dbfx"
        this.navigator = CustomTreeView.customTreeView
        this.splitPane.items.add(this.navigator)
        this.tabHandler = MainTabPaneHandler.handler
        this.splitPane.items.add(tabHandler.getTabPane())
        this.eventLog.graphic = SvgImageTranscoder.svgToImageView(N_EVENT_LOG)
        this.terminal.graphic = SvgImageTranscoder.svgToImageView(N_TERMINAL)
        this.expandPaneController = BottomNavigationExpandPaneController()
        //Listener whether bottom expand pane should show
        this.expandPaneController.panePropertyProperty().addListener { _, _, obj ->
            if (obj == null) {
                this.mSPane.items.remove(expandPaneController.parent)
            } else {
                if (this.mSPane.items.size > 1) {
                    return@addListener
                }
                this.mSPane.items.add(expandPaneController.parent)
            }
        }
    }

    @FXML
    fun about() {
        AboutView()
    }

    @FXML
    fun createCon() {
        CreateConView()
    }

    @FXML
    fun openSQLTerminal() {
        val temp = navigator.selectionModel.selectedItem
        if (temp == null) {
            AlertUtils.showSimpleDialog("未选中任何数据库节点!")
            return
        }
        val selectItem = temp as BaseTreeItem
        try {
            val path = selectItem.fullPath
            val client = selectItem.currentClient
            val terminal = SQLTerminalTab(client)
            GlobalScope.launch { MainTabPaneHandler.handler.addTabToPane(terminal, path) }
        } catch (e: Exception) {
            AlertUtils.showSimpleDialog("请确保当前数据库节点已经打开连接!")
        }
    }

    @FXML
    fun openTerminal() {
        this.expandPaneController.paneProperty = TerminalController()
    }

    @FXML
    fun openLog() {
        this.expandPaneController.paneProperty = LogController()
    }


    override fun onClose(event: WindowEvent) {
        val result = AlertUtils.showSimpleConfirmDialog("你确定要退出应用程序?")
        if (!result) {
            event.consume()
            return
        }
        closeAppOccurResource()
    }
}
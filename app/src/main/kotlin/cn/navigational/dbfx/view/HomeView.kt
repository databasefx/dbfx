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
import cn.navigational.dbfx.kit.i18n.I18N
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.utils.AlertUtils
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.SplitPane
import javafx.stage.WindowEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeView private constructor() : View<Void>(HOME_PAGE) {
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
        this.title = I18N.getString("stage.home")
        this.navigator = CustomTreeView.customTreeView
        this.splitPane.items.add(this.navigator)
        this.tabHandler = MainTabPaneHandler.handler
        this.splitPane.items.add(tabHandler.getTabPane())
        this.eventLog.graphic = SvgImageTranscoder.svgToImageView(N_EVENT_LOG)
//        this.terminal.graphic = SvgImageTranscoder.svgToImageView(N_TERMINAL)
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
    fun exit() {
        onClose(null)
    }

    @FXML
    fun openSQLTerminal() {
        val temp = navigator.selectionModel.selectedItem
        if (temp == null) {
            AlertUtils.showSimpleDialog(I18N.getString("alert.please.client"))
            return
        }
        val selectItem = temp as BaseTreeItem
        try {
            val path = selectItem.fullPath
            val client = selectItem.currentClient
            val terminal = SQLTerminalTab(client)
            GlobalScope.launch { MainTabPaneHandler.handler.addTabToPane(terminal, path) }
        } catch (e: Exception) {
            AlertUtils.showSimpleDialog(I18N.getString("alert.sure.open.connection"))
        }
    }

    @FXML
    fun openLog() {
        this.expandPaneController.paneProperty = LogController()
    }


    override fun onClose(event: WindowEvent?) {
        val result = AlertUtils.showSimpleConfirmDialog(I18N.getString("alert.application.exit.tips"))
        if (!result) {
            event?.consume()
            return
        }
        closeAppOccurResource()
    }

    companion object {
        val home: HomeView by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { HomeView() }
    }
}
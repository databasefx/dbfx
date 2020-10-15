package cn.navigational.dbfx.view

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.ViewController
import cn.navigational.dbfx.config.HOME_PAGE
import cn.navigational.dbfx.config.N_EVENT_LOG
import cn.navigational.dbfx.controller.BottomNavigationExpandPaneAbstractFxmlController
import cn.navigational.dbfx.controller.LogAbstractFxmlController
import cn.navigational.dbfx.controls.tab.SQLTerminalTab
import cn.navigational.dbfx.handler.MainTabPaneHandler
import cn.navigational.dbfx.controls.tree.CustomTreeView
import cn.navigational.dbfx.handler.closeAppOccurResource
import cn.navigational.dbfx.i18n.I18N
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.utils.AlertUtils
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import javafx.stage.WindowEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewController private constructor() : ViewController<BorderPane>(HOME_PAGE) {
    @FXML
    private lateinit var eventLog: Button

    @FXML
    private lateinit var mSPane: SplitPane

    @FXML
    private lateinit var splitPane: SplitPane

    private val navigator: CustomTreeView = CustomTreeView.customTreeView

    private val tabHandler: MainTabPaneHandler = MainTabPaneHandler.handler

    private val expandPaneController: BottomNavigationExpandPaneAbstractFxmlController = BottomNavigationExpandPaneAbstractFxmlController()

    init {
        this.splitPane.items.add(this.navigator)
        this.splitPane.items.add(tabHandler.getTabPane())
        this.eventLog.graphic = SvgImageTranscoder.svgToImageView(N_EVENT_LOG)
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
        this.setSizeWithScreen(0.8, 0.9)
        this.stage.title = I18N.getString("stage.home")
    }

    @FXML
    fun about() {
        AboutViewController().showStage()
    }

    @FXML
    fun createCon() {
        CreateConViewController().showStage()
    }

    @FXML
    fun exit() {
        onCloseRequest(null)
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
        this.expandPaneController.paneProperty = LogAbstractFxmlController()
    }


    override fun onCloseRequest(event: WindowEvent?) {
        val result = AlertUtils.showSimpleConfirmDialog(I18N.getString("alert.application.exit.tips"))
        if (!result) {
            event?.consume()
            return
        }
        closeAppOccurResource()
    }

    companion object {
        val home: HomeViewController by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { HomeViewController() }
    }
}
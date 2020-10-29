package cn.navigational.dbfx.view

import cn.navigational.dbfx.ViewController
import cn.navigational.dbfx.config.*
import cn.navigational.dbfx.controller.NavigatorToolBarController
import cn.navigational.dbfx.controls.tree.CustomTreeView
import cn.navigational.dbfx.handler.MainTabPaneHandler
import cn.navigational.dbfx.handler.closeAppOccurResource
import cn.navigational.dbfx.i18n.I18N
import cn.navigational.dbfx.utils.AlertUtils
import javafx.fxml.FXML
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.WindowEvent

class HomeViewController private constructor() : ViewController<BorderPane>(HOME_PAGE) {

    @FXML
    private lateinit var splitPane: SplitPane

    @FXML
    private lateinit var leftBox: VBox

    private val navigator: CustomTreeView = CustomTreeView.getNavigator()

    init {
        VBox.setVgrow(this.navigator, Priority.ALWAYS)
        this.leftBox.children.addAll(this.navigator.controlBar, this.navigator)
        this.splitPane.items.add(MainTabPaneHandler.getTabPane())

        //Listener whether bottom expand pane should show

        this.setSizeWithScreen(0.8, 0.9)
        this.stage.title = I18N.getString("stage.home")
    }

    @FXML
    fun about() {
        AboutViewController().showStage()
    }


    @FXML
    fun exit() {
        onCloseRequest(null)
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
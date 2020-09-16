package cn.navigational.dbfx.view

import cn.navigational.dbfx.View
import cn.navigational.dbfx.config.HOME_PAGE
import cn.navigational.dbfx.handler.MainTabPaneHandler
import cn.navigational.dbfx.controls.tree.CustomTreeView
import cn.navigational.dbfx.handler.closeAppOccurResource
import cn.navigational.dbfx.utils.AlertUtils
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.SplitPane
import javafx.stage.WindowEvent

class HomeView : View<Void>(HOME_PAGE) {

    @FXML
    private lateinit var splitPane: SplitPane

    private lateinit var navigator: CustomTreeView

    private lateinit var tabHandler: MainTabPaneHandler


    override fun onCreated(scene: Scene?) {
        this.navigator = CustomTreeView.customTreeView
        this.splitPane.items.add(this.navigator)
        this.tabHandler = MainTabPaneHandler.handler
        this.splitPane.items.add(tabHandler.getTabPane())
        this.title = "dbfx"
    }

    @FXML
    fun about() {
    }

    @FXML
    fun createCon() {
        CreateConView()
    }


    override fun onClose(event: WindowEvent) {
        val result = AlertUtils.showSimpleConfirmDialog("你确定要退出应用程序?")
        if (!result) {
            event.consume()
        }
        closeAppOccurResource()
    }
}
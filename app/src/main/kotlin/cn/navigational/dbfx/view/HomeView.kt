package cn.navigational.dbfx.view

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.View
import cn.navigational.dbfx.config.HOME_PAGE
import cn.navigational.dbfx.controller.MainTabPaneController
import cn.navigational.dbfx.controller.ProgressDialogController
import cn.navigational.dbfx.controls.tree.CustomTreeView
import cn.navigational.dbfx.model.DbInfo
import cn.navigational.dbfx.controls.tree.NTreeCell
import cn.navigational.dbfx.navigator.impl.MysqlItem
import cn.navigational.dbfx.navigator.impl.PgItem
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.utils.AlertUtils
import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.SplitPane
import javafx.scene.control.TreeItem
import javafx.stage.WindowEvent

class HomeView : View<Void>(HOME_PAGE) {
    @FXML
    private lateinit var navigator: CustomTreeView

    @FXML
    private lateinit var splitPane: SplitPane

    private lateinit var tabController: MainTabPaneController


    override fun onCreated(scene: Scene?) {
        this.title = "dbfx"
        val that = this
        this.tabController = MainTabPaneController.getController()
        this.splitPane.items.add(tabController.getTabPane())
        navigator.contextMenu = null
        navigator.setCellFactory { NTreeCell() }
        SQLClientManager.getDbInfo().forEach(this::createClientTree)
        SQLClientManager.getDbInfo().addListener(ListChangeListener {
            while (it.next()) {
                if (it.wasAdded()) {
                    it.addedSubList.forEach(that::createClientTree)
                }
            }
        })
    }

    private fun createClientTree(it: DbInfo) {
        val item: BaseTreeItem<String> = if (it.client == Clients.MYSQL) {
            MysqlItem(it)
        } else {
            PgItem(it)
        }
        if (navigator.root == null) {
            navigator.root = TreeItem()
        }
        navigator.root.children.add(item)
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
        //Execute close resource operation
        ProgressDialogController()
    }
}
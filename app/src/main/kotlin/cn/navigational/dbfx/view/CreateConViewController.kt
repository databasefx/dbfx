package cn.navigational.dbfx.view

import cn.navigational.dbfx.DatabaseMetaManager
import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.ViewController
import cn.navigational.dbfx.config.CREATE_CON_PAGE
import cn.navigational.dbfx.controller.ConInfoPaneController
import cn.navigational.dbfx.model.DatabaseMeta
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.SqlClientFactory
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.i18n.I18N
import cn.navigational.dbfx.utils.AlertUtils
import cn.navigational.dbfx.kit.utils.StringUtils
import cn.navigational.dbfx.kit.utils.VertxUtils
import cn.navigational.dbfx.model.DbInfo
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import io.vertx.sqlclient.SqlConnectOptions
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Pagination
import javafx.scene.control.ScrollPane
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import javafx.stage.Modality
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class CreateConViewController(private val cl: Clients) : ViewController<ScrollPane>(CREATE_CON_PAGE) {
    @FXML
    private lateinit var last: Button

    @FXML
    private lateinit var next: Button

    @FXML
    private lateinit var test: Button

    @FXML
    private lateinit var finish: Button

    private val conInfoPaneController = ConInfoPaneController()


    init {
        this.stage.isResizable = false
        this.stage.initModality(Modality.APPLICATION_MODAL)
        this.setSizeWithScreen(0.5, 0.7)
        this.stage.title = I18N.getString("stage.create.connection")
        this.conInfoPaneController.initMeta(DatabaseMetaManager.getDbMeta(cl))
        (this.parent.content as BorderPane).center = conInfoPaneController.parent

    }

    constructor(info: DbInfo) : this(info.client) {
        conInfoPaneController.initEdit(info)
    }


    @FXML
    fun cancel() {
        this.stage.close()
    }

    @FXML
    fun testCon() {
        val meta = DatabaseMetaManager.getDbMeta(cl)
        val info = conInfoPaneController.getDbInfo(meta)
        if (StringUtils.isEmpty(info.host)) {
            AlertUtils.showSimpleDialog("主机不能为空")
            return
        }
        val cl = info.client
        val options: SqlConnectOptions = SqlClientFactory.createConnectionOptions(cl)
        options.host = info.host
        options.user = info.username
        options.database = info.database
        if (StringUtils.isNotEmpty(info.password)) {
            options.password = info.password
        }
        val client = SqlClientFactory.createClient(VertxUtils.getVertx(), options, cl)
        GlobalScope.launch {
            val version: String
            try {
                version = SQLQuery.getClQuery(cl).showDbVersion(client)
            } catch (e: Exception) {
                AlertUtils.showExDialog("连接失败", e)
                return@launch
            }
            AlertUtils.showSimpleDialog("${cl.client}($version)")
            SQLClientManager.closeSQLClient(client)
        }
    }

    @FXML
    private fun finish() {
        var meta = DatabaseMetaManager.getDbMeta(cl)
        val info = conInfoPaneController.getDbInfo(meta)
        if (StringUtils.isEmpty(info.name)) {
            AlertUtils.showSimpleDialog("连接名不能为空")
            return
        }
        if (StringUtils.isEmpty(info.host)) {
            AlertUtils.showSimpleDialog("主机名不能为空")
            return
        }
        if (info.port <= 0) {
            AlertUtils.showSimpleDialog("端口值不正确")
            return
        }
        if (StringUtils.isEmpty(info.username)) {
            AlertUtils.showSimpleDialog("用户名不能为空")
            return
        }
        GlobalScope.launch {
            info.uuid = StringUtils.uuid()
            SQLClientManager.updateDbInfo(info)
            Platform.runLater {
                stage.close()
            }
        }

    }

//    private fun initFlow() {
//        firstPane.styleClass.add("first-pane")
//        val items = DatabaseMetaManager.getMetas().map { FlowItem(it) }
//        firstPane.children.addAll(items)
//    }

//    private fun selectChange(item: FlowItem) {
//        val items = firstPane.children
//        val index = items.indexOf(item)
//        if (selectIndex == -1) {
//            next.isDisable = false
//        }
//        if (selectIndex != -1) {
//            items[selectIndex].styleClass.remove("active")
//        }
//        item.styleClass.add("active")
//        selectIndex = index
//    }
}
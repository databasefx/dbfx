package cn.navigational.dbfx.view

import cn.navigational.dbfx.DatabaseMetaManager
import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.View
import cn.navigational.dbfx.config.CREATE_CON_PAGE
import cn.navigational.dbfx.controller.ConInfoPaneController
import cn.navigational.dbfx.model.DatabaseMeta
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.SqlClientFactory
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.utils.AlertUtils
import cn.navigational.dbfx.kit.utils.StringUtils
import cn.navigational.dbfx.kit.utils.VertxUtils
import cn.navigational.dbfx.io.saveDbInfo
import io.vertx.sqlclient.SqlConnectOptions
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Pagination
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import javafx.stage.Modality
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class CreateConView : View<Void>(CREATE_CON_PAGE) {
    @FXML
    private lateinit var last: Button

    @FXML
    private lateinit var next: Button

    @FXML
    private lateinit var page: Pagination

    @FXML
    private lateinit var test: Button

    @FXML
    private lateinit var finish: Button

    private lateinit var firstPane: FlowPane

    private val conInfoPaneController = ConInfoPaneController()

    private val nextPane: BorderPane = conInfoPaneController.parent

    /**
     * Default no select any so that value -1
     */
    private var selectIndex: Int = -1

    override fun onCreated(scene: Scene?) {
        initFlow()
        next.setOnAction {
            page.currentPageIndex = 1
            val item = firstPane.children[selectIndex] as FlowItem
            conInfoPaneController.initMeta(item.getDBMeta())
        }
        last.setOnAction {
            page.currentPageIndex = 0
        }
        page.setPageFactory {
            pageFactory(it)
        }
        page.currentPageIndexProperty().addListener { _, _, n ->
            if (n == 1) {
                finish.isDisable = false
                next.isDisable = true
                last.isDisable = false
                test.isDisable = false
            } else {
                finish.isDisable = true
                next.isDisable = false
                last.isDisable = true
                test.isDisable = true

            }
        }
        this.isResizable = false
        this.initModality(Modality.APPLICATION_MODAL)
        title = "创建新连接"
    }

    @FXML
    fun pageFactory(index: Int): Node? {
        return if (index == 0)
            firstPane
        else
            nextPane
    }

    @FXML
    fun cancel() {
        this.close()
    }

    @FXML
    fun testCon() {
        val meta = (firstPane.children[selectIndex] as FlowItem).getDBMeta()
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
            SQLClientManager.manager.closeSQLClient(client)
        }
    }

    @FXML
    private fun finish() {
        val item = firstPane.children[selectIndex] as FlowItem
        val info = conInfoPaneController.getDbInfo(item.getDBMeta())
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
        val that = this
        GlobalScope.launch {
            info.uuid = StringUtils.uuid()
            if (info.local) {
                saveDbInfo(info)
            }
            SQLClientManager.manager.addDbInfo(info)
            Platform.runLater {
                that.close()
            }
        }

    }

    private fun initFlow() {
        firstPane = FlowPane()
        firstPane.styleClass.add("first-pane")
        val items = DatabaseMetaManager.getMetas().map { FlowItem(it) }
        firstPane.children.addAll(items)
    }

    private fun selectChange(item: FlowItem) {
        val items = firstPane.children
        val index = items.indexOf(item)
        if (selectIndex == -1) {
            next.isDisable = false
        }
        if (selectIndex != -1) {
            items[selectIndex].styleClass.remove("active")
        }
        item.styleClass.add("active")
        selectIndex = index
    }

    inner class FlowItem(db: DatabaseMeta) : VBox() {

        private val label: Label = Label()
        private val imageView: ImageView = ImageView()
        private val support: Boolean = db.support
        private val client: Clients = Clients.getClient(db.name)
        private val meta: DatabaseMeta = db

        init {
            val icon = db.icon
            if (icon != null && icon != "") {
                val input = ClassLoader.getSystemResourceAsStream(icon)
                val image = Image(input, 100.0, 80.0, false, true)
                imageView.image = image
            }
            label.text = client.name
            this.children.addAll(imageView, label)
            this.styleClass.add("flow-item")
            this.setOnMouseClicked {
                selectChange(this)
            }
        }

        fun getDBMeta(): DatabaseMeta {
            return meta
        }

        fun getClient(): Clients {
            return client
        }

        fun getSupport(): Boolean {
            return support;
        }
    }
}
package cn.navigational.dbfx.controller

import cn.navigational.dbfx.Controller
import cn.navigational.dbfx.DatabaseMetaManager
import cn.navigational.dbfx.config.CON_INFO_PANE
import cn.navigational.dbfx.model.DatabaseMeta
import cn.navigational.dbfx.model.DbInfo
import cn.navigational.dbfx.kit.enums.Clients
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane

class ConInfoPaneController : Controller<Void, BorderPane>(CON_INFO_PANE) {
    @FXML
    private lateinit var name: TextField

    @FXML
    private lateinit var host: TextField

    @FXML
    private lateinit var icon: ImageView

    @FXML
    private lateinit var port: TextField

    @FXML
    private lateinit var dDescribe: Label

    @FXML
    private lateinit var database: TextField

    @FXML
    private lateinit var local: CheckBox

    @FXML
    private lateinit var comment: TextField

    @FXML
    private lateinit var username: TextField

    @FXML
    private lateinit var password: PasswordField


    override fun onCreated(root: BorderPane?) {

    }

    fun initMeta(meta: DatabaseMeta) {
        password.text = ""
        local.isSelected = true
        host.text = "localhost"
        database.text = meta.database
        port.text = meta.port.toString()
        username.text = meta.username
        dDescribe.text = "Create a ${meta.name} Database Connection."
        this.updateIcon(meta.icon)
    }

    fun initEdit(info: DbInfo) {
        val meta = DatabaseMetaManager.manager.getDbMeta(info.client)
        this.updateIcon(meta.icon)
        this.dDescribe.text = "Edit a ${meta.name} Database Connection."
        this.name.text = info.name
        this.local.isSelected = info.local
        this.password.text = info.password
        this.host.text = info.host
        this.port.text = info.port.toString()
        this.database.text = info.database
        this.comment.text = info.comment
        this.username.text = info.username
        info.client
    }

    fun getDbInfo(meta: DatabaseMeta): DbInfo {
        val info = DbInfo()
        info.name = name.text
        info.client = Clients.getClient(meta.name)
        info.comment = comment.text
        info.host = host.text
        info.password = password.text
        info.port = port.text.toInt()
        info.local = local.isSelected
        info.username = username.text
        var database = database.text
        //If not init database,use default database init.
        if (database.trim() == "") {
            database = meta.database
        }
        info.database = database
        return info
    }

    private fun updateIcon(path: String) {
        val im = ClassLoader.getSystemResourceAsStream(path)
        icon.image = Image(im, 130.0, 110.0, false, true)
    }
}
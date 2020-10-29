package cn.navigational.dbfx.controls.tab

import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.controller.SQLTerminalController
import cn.navigational.dbfx.controls.AbstractBaseTab
import cn.navigational.dbfx.model.SQLClient
import javafx.application.Platform

class SQLTerminalTab(private val uuid: String, private val scheme: String = "") : AbstractBaseTab(SQLClient.getMiniIcon(SQLClientManager.getDbInfo(uuid).client)) {

    private lateinit var controller: SQLTerminalController

    override suspend fun init() {
        this.controller = SQLTerminalController(uuid, scheme)
        val info = SQLClientManager.getDbInfo(uuid)
        Platform.runLater {
            this.content = controller.parent
            this.text = "console [${scheme}@${info.host}"
        }
    }

    override suspend fun close() {
        this.controller.dispose()
    }
}
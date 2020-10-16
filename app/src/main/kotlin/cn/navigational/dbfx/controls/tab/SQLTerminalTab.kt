package cn.navigational.dbfx.controls.tab

import cn.navigational.dbfx.controller.SQLTerminalController
import cn.navigational.dbfx.controls.AbstractBaseTab
import cn.navigational.dbfx.model.SQLClient

class SQLTerminalTab(client: SQLClient) : AbstractBaseTab(SQLClient.getMiniIcon(client.cl)) {
    private val controller = SQLTerminalController()

    init {
        val info = client.dbInfo
        this.content = controller.parent
        this.text = "console [${info.database}@${info.host}"
    }

    override suspend fun init() {

    }
}
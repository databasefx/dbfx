package cn.navigational.dbfx.navigator.impl

import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.config.PG_ICON
import cn.navigational.dbfx.model.DbInfo
import cn.navigational.dbfx.navigator.DatabaseItem
import cn.navigational.dbfx.navigator.folder.pg.PgRoleFolder
import cn.navigational.dbfx.navigator.folder.pg.PgDbFolder
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients

class PgItem(info: DbInfo) : DatabaseItem(info, PG_ICON) {

    private val schemeFolder = PgDbFolder()
    private val pgRoleFolder = PgRoleFolder()

    override suspend fun startConnect() {
        this.initClient()
        val query = SQLQuery.getClQuery(Clients.POSTGRESQL)
        getSQLClient()!!.version = query.showDbVersion(getSQLClient()!!.client)
        SQLClientManager.manager.addClient(getSQLClient()!!)
        this.flush()
        connectStatus.value = true
    }

    override suspend fun flush() {
        if (!this.connectStatus.value) {
            children.addAll(schemeFolder, pgRoleFolder)
        }
        schemeFolder.initFolder()
        pgRoleFolder.initFolder()
    }
}
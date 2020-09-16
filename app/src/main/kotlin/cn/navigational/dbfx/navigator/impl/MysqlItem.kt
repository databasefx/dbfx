package cn.navigational.dbfx.navigator.impl

import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.config.MYSQL_ICON
import cn.navigational.dbfx.model.DbInfo
import cn.navigational.dbfx.navigator.DatabaseItem
import cn.navigational.dbfx.navigator.folder.mysql.MyCollationFolder
import cn.navigational.dbfx.navigator.folder.mysql.MySchemeFolder
import cn.navigational.dbfx.navigator.folder.mysql.MyUserFolder
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients

class MysqlItem(uuid: String) : DatabaseItem(uuid, MYSQL_ICON) {
    private val userFolder = MyUserFolder()
    private val schemeFolder = MySchemeFolder()
    private val collationFolder = MyCollationFolder()

    override suspend fun startConnect() {
        this.initClient()
        val query = SQLQuery.getClQuery(Clients.MYSQL)
        getSQLClient().version = query.showDbVersion(getSQLClient().client)
        SQLClientManager.manager.addClient(getSQLClient())
        this.flush()
        connectStatus.value = true
    }

    override suspend fun flush() {
        if (!this.connectStatus.value) {
            children.addAll(schemeFolder, collationFolder, userFolder)
        }
        schemeFolder.initFolder()
        collationFolder.initFolder()
        userFolder.initFolder()
    }
}
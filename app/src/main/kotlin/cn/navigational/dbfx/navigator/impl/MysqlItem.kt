package cn.navigational.dbfx.navigator.impl

import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.config.MYSQL_ICON
import cn.navigational.dbfx.navigator.DatabaseItem
import cn.navigational.dbfx.navigator.folder.mysql.MyCollationFolder
import cn.navigational.dbfx.navigator.folder.mysql.MySchemeFolder
import cn.navigational.dbfx.navigator.folder.mysql.MyUserFolder
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients

class MysqlItem(uuid: String) : DatabaseItem(uuid, MYSQL_ICON) {
    override suspend fun startConnect() {
        this.initClient()
        val query = SQLQuery.getClQuery(Clients.MYSQL)
        getSQLClient().version = query.showDbVersion(getSQLClient().client)
        SQLClientManager.manager.addClient(getSQLClient())
        this.flush()
        connectStatus.value = true
    }

    override suspend fun flush() {
        this.children.clear()
        val userFolder = MyUserFolder()
        val schemeFolder = MySchemeFolder()
        val collationFolder = MyCollationFolder()
        this.children.addAll(schemeFolder, collationFolder, userFolder)
        userFolder.initFolder()
        schemeFolder.initFolder()
        collationFolder.initFolder()
    }
}
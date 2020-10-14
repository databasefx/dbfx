package cn.navigational.dbfx.navigator.folder.mysql

import cn.navigational.dbfx.navigator.FolderItem
import cn.navigational.dbfx.navigator.scheme.ComSchemeItem
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.i18n.I18N
import cn.navigational.dbfx.kit.utils.StringUtils

class MySchemeFolder : FolderItem() {
    init {
        value = I18N.getString("label.database")
    }

    override suspend fun initFolder() {
        if (this.children.isNotEmpty()) {
            this.children.clear()
        }
        val list = SQLQuery.getClQuery(Clients.MYSQL).showDatabase(currentClient.client)
        //if specify database->show specify database
        val database = currentClient.dbInfo.database
        if (StringUtils.isNotEmpty(database)) {
            list.contains(database).also {
                if (it)
                    this.children.add(ComSchemeItem(Clients.MYSQL, database))
            }
            return
        }
        this.children.addAll(list.map { ComSchemeItem(Clients.MYSQL, it) })
    }
}
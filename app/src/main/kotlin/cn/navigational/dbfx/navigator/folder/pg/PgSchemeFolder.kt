package cn.navigational.dbfx.navigator.folder.pg

import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.i18n.I18N
import cn.navigational.dbfx.kit.postgres.PgQuery
import cn.navigational.dbfx.navigator.FolderItem
import cn.navigational.dbfx.navigator.scheme.ComSchemeItem

class PgSchemeFolder(private val database: String) : FolderItem() {
    init {
        value = I18N.getString("label.scheme")
    }

    override suspend fun initFolder() {
        if (this.children.isNotEmpty()) {
            this.children.clear()
        }
        val query = SQLQuery.getClQuery(Clients.POSTGRESQL) as PgQuery
        val list = query.queryDbScheme(database, currentClient.client)
        val items = list.map {
            ComSchemeItem(Clients.POSTGRESQL, "${database}.$it")
        }.toList()
        this.children.addAll(items)
    }

}
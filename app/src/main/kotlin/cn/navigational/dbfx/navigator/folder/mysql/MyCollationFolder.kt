package cn.navigational.dbfx.navigator.folder.mysql

import cn.navigational.dbfx.navigator.FolderItem
import cn.navigational.dbfx.navigator.collation.MyCollationItem
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.i18n.I18N

class MyCollationFolder : FolderItem() {
    init {
        value = I18N.getString("label.collations")
    }

    override suspend fun initFolder() {
        val list = SQLQuery.getClQuery(Clients.MYSQL).showCollations(currentClient.client)
        val items = list.map { MyCollationItem(it) }
        this.children.addAll(items)
    }
}
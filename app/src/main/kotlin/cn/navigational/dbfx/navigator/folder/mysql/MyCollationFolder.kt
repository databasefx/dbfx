package cn.navigational.dbfx.navigator.folder.mysql

import cn.navigational.dbfx.navigator.FolderItem
import cn.navigational.dbfx.navigator.collation.MyCollationItem
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients

class MyCollationFolder : FolderItem() {
    init {
        value = "排序规则"
    }

    override suspend fun initFolder() {
        val list = SQLQuery.getClQuery(Clients.MYSQL).showCollations(currentClient.client)
        val items = list.map { MyCollationItem(it) }
        this.children.addAll(items)
    }
}
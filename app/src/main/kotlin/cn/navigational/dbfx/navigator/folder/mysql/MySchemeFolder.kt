package cn.navigational.dbfx.navigator.folder.mysql

import cn.navigational.dbfx.navigator.FolderItem
import cn.navigational.dbfx.navigator.scheme.MySchemeItem
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients

class MySchemeFolder : FolderItem() {
    init {
        value = "数据库"
    }

    override suspend fun initFolder() {
        val list = SQLQuery.getClQuery(Clients.MYSQL).showDatabase(currentClient.client)
        val ss = arrayListOf<MySchemeItem>()
        for (s in list) {
            val my = MySchemeItem(s)
            ss.add(my)
        }
        if (this.children.isNotEmpty()) {
            this.children.clear()
        }
        this.children.addAll(ss)
    }
}
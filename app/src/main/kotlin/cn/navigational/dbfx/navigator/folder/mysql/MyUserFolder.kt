package cn.navigational.dbfx.navigator.folder.mysql

import cn.navigational.dbfx.navigator.FolderItem
import cn.navigational.dbfx.navigator.user.MyUserItem
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients

class MyUserFolder : FolderItem() {
    init {
        value = "用户"
    }

    override suspend fun initFolder() {
        val temp = SQLQuery.getClQuery(Clients.MYSQL).queryDbUser(currentClient.client)
        val list = temp.map { MyUserItem(it) }
        this.children.addAll(list)
    }
}
package cn.navigational.dbfx.navigator.folder.pg

import cn.navigational.dbfx.navigator.FolderItem
import cn.navigational.dbfx.navigator.user.PgRoleItem
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients

class PgRoleFolder : FolderItem() {
    init {
        value = "角色"
    }

    override suspend fun initFolder() {
        if (this.children.isNotEmpty()) {
            this.children.clear()
        }
        val list = SQLQuery.getClQuery(Clients.POSTGRESQL).queryDbUser(currentClient.client)
                .map { PgRoleItem(it) }
        this.children.addAll(list)
    }
}
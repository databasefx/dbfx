package cn.navigational.dbfx.navigator.folder

import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.navigator.FolderItem

class ViewFolder(clients: Clients, category: String) : FolderItem() {
    init {
        value = "视图"
    }

    override suspend fun initFolder() {
    }
}
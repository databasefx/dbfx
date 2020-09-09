package cn.navigational.dbfx.navigator.folder.pg

import cn.navigational.dbfx.navigator.FolderItem

class PgExtensionFolder : FolderItem() {
    init {
        value = "扩展"
    }
    override suspend fun initFolder() {
    }

}
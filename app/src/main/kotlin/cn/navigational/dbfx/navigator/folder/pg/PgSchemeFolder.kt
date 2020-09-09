package cn.navigational.dbfx.navigator.folder.pg

import cn.navigational.dbfx.navigator.FolderItem

class PgSchemeFolder : FolderItem() {
    init {
        value = "模式"
    }
    override suspend fun initFolder() {

    }

}
package cn.navigational.dbfx.navigator.folder.pg

import cn.navigational.dbfx.navigator.FolderItem

class PgAcMethodFolder : FolderItem() {
    init {
        value = "访问方法"
    }

    override suspend fun initFolder() {
    }
}
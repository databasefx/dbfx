package cn.navigational.dbfx.navigator.folder.pg

import cn.navigational.dbfx.navigator.FolderItem

class PgLanguageFolder : FolderItem(){
    init {
        value = "语言"
    }
    override suspend fun initFolder() {
    }
}
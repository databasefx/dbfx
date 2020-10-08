package cn.navigational.dbfx.navigator.scheme

import cn.navigational.dbfx.config.PG_DB_ICON
import cn.navigational.dbfx.navigator.SchemeItem
import cn.navigational.dbfx.navigator.folder.pg.PgSchemeFolder

class PgDbItem(private val dbName: String) : SchemeItem(PG_DB_ICON, dbName) {

    init {
        value = dbName
    }

    override suspend fun initData() {
        if (this.children.isNotEmpty()) {
            this.children.clear()
        }
        val schemeFolder = PgSchemeFolder(dbName)
        this.children.add(schemeFolder)
        schemeFolder.initFolder()
        loadStatusProperty().set(true)
    }
}
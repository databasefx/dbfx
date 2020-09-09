package cn.navigational.dbfx.navigator.scheme

import cn.navigational.dbfx.config.PG_DB_ICON
import cn.navigational.dbfx.navigator.SchemeItem
import cn.navigational.dbfx.navigator.folder.pg.PgExtensionFolder
import cn.navigational.dbfx.navigator.folder.pg.PgAcMethodFolder
import cn.navigational.dbfx.navigator.folder.pg.PgLanguageFolder
import cn.navigational.dbfx.navigator.folder.pg.PgSchemeFolder

class PgDbItem(private val dbName: String) : SchemeItem(PG_DB_ICON, dbName) {
    private val indexFolder = PgAcMethodFolder()
    private val langFolder = PgLanguageFolder()
    private val schemeFolder = PgSchemeFolder()
    private val extensionFolder = PgExtensionFolder()

    init {
        value = dbName
    }

    override suspend fun initData() {
        if (this.children.isEmpty()) {
            this.children.addAll(schemeFolder, indexFolder, extensionFolder, langFolder)
        }
        langFolder.initFolder()
        indexFolder.initFolder()
        schemeFolder.initFolder()
        extensionFolder.initFolder()
    }
}
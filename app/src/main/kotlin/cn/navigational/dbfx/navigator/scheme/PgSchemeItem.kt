package cn.navigational.dbfx.navigator.scheme

import cn.navigational.dbfx.config.SCHEME_ICON
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.navigator.SchemeItem
import cn.navigational.dbfx.navigator.folder.TableFolder
import cn.navigational.dbfx.navigator.folder.ViewFolder

class PgSchemeItem(val scheme: String) : SchemeItem(SCHEME_ICON, scheme) {
    init {
        value = scheme.split('.')[1]
    }

    override suspend fun initData() {
        if (this.children.isNotEmpty()) {
            this.children.clear()
        }
        val viewFolder = ViewFolder(Clients.POSTGRESQL, scheme)
        val tableFolder = TableFolder(Clients.POSTGRESQL, scheme)
        this.children.addAll(tableFolder, viewFolder)
        viewFolder.initFolder()
        tableFolder.initFolder()
    }
}
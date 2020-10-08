package cn.navigational.dbfx.navigator.scheme

import cn.navigational.dbfx.config.SCHEME_ICON
import cn.navigational.dbfx.navigator.SchemeItem
import cn.navigational.dbfx.navigator.table.TableItem
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.navigator.folder.TableFolder
import cn.navigational.dbfx.navigator.folder.ViewFolder

class ComSchemeItem(private val clients: Clients, private val scheme: String) : SchemeItem(SCHEME_ICON, scheme) {
    init {
        value = getShowValue()
    }

    override suspend fun initData() {
        if (this.children.isNotEmpty()) {
            this.children.clear()
        }
        val viewFolder = ViewFolder(clients, scheme)
        val tableFolder = TableFolder(clients, scheme)
        this.children.addAll(tableFolder, viewFolder)
        viewFolder.initFolder()
        tableFolder.initFolder()
        if (!isLoadStatus) {
            isLoadStatus = true
        }
    }

    private fun getShowValue(): String {
        return when (clients) {
            Clients.POSTGRESQL -> scheme.split(".")[1]
            else -> scheme
        }
    }
}
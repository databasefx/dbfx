package cn.navigational.dbfx.navigator.folder

import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.navigator.FolderItem
import cn.navigational.dbfx.navigator.table.TableItem

class TableFolder(val clients: Clients, private val category: String) : FolderItem() {
    init {
        value = "表"
    }

    override suspend fun initFolder() {
        if (this.children.isNotEmpty()) {
            this.children.clear()
        }
        val query = SQLQuery.getClQuery(clients)
        val list = query.showTable(category, currentClient.client)
        val tables = list.map { TableItem(it, category, clients) }.toList()
        this.children.addAll(tables)
        tables.forEach { it.initField() }
        loadStatusProperty().set(true)
    }
}
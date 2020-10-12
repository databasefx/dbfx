package cn.navigational.dbfx.navigator.folder

import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.i18n.I18N
import cn.navigational.dbfx.navigator.FolderItem
import cn.navigational.dbfx.navigator.table.TableItem

class ViewFolder(private val clients: Clients, private val category: String) : FolderItem() {
    init {
        value = I18N.getString("label.view")
    }

    override suspend fun initFolder() {
        val query = SQLQuery.getClQuery(clients)
        val list = query.showView(category, currentClient.client)
        val views = list.map { TableItem(it, category, clients, TableItem.TableType.VIEW) }.toList()
        this.children.addAll(views)
        views.forEach { it.initField() }
    }
}
package cn.navigational.dbfx.navigator.scheme

import cn.navigational.dbfx.config.SCHEME_ICON
import cn.navigational.dbfx.navigator.SchemeItem
import cn.navigational.dbfx.navigator.table.TableItem
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients

class MySchemeItem(private val scheme: String) : SchemeItem(SCHEME_ICON, scheme) {

    override suspend fun initData() {
        if (this.children.isNotEmpty()) {
            this.children.clear()
        }
        val sqlQuery = SQLQuery.getClQuery(Clients.MYSQL)
        val list = sqlQuery.showTable(scheme, currentClient.client).map { TableItem(it, scheme, Clients.MYSQL) }
        this.children.addAll(list)
        list.forEach { it.initField() }
        if (!isLoadStatus) {
            isLoadStatus = true
        }
    }
}
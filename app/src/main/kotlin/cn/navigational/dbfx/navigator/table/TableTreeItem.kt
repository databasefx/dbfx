package cn.navigational.dbfx.navigator.table

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.config.TABLE_ICON
import cn.navigational.dbfx.handler.MainTabPaneHandler
import cn.navigational.dbfx.controls.tab.TableTab
import cn.navigational.dbfx.handler.NavigatorMenuHandler
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients


class TableTreeItem(private val table: String, private val category: String, private val cl: Clients) : BaseTreeItem<String>() {
    init {
        this.value = table
        setIcon(TABLE_ICON)
        val handler = NavigatorMenuHandler.init(supportMenu)
        handler.getMenuItem("打开", NavigatorMenuHandler.Companion.MenuType.OPEN, this::openTab)
    }

    suspend fun initField() {
        val list = SQLQuery.getClQuery(cl)
                .showTableField(category, table, currentClient.client).map {
                    TableFieldItem(it, cl)
                }
        this.children.addAll(list)
    }

    private suspend fun openTab() {
        var tab = TableTab(table, category, currentClient)
        tab =  MainTabPaneHandler.handler.addTabToPane(tab, fullPath) as TableTab
    }
}
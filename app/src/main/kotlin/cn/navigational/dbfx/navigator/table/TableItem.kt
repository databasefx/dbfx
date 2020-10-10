package cn.navigational.dbfx.navigator.table

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.config.TABLE_ICON
import cn.navigational.dbfx.config.TABLE_VIEW_ICON
import cn.navigational.dbfx.handler.MainTabPaneHandler
import cn.navigational.dbfx.controls.tab.TableTab
import cn.navigational.dbfx.handler.NavigatorMenuHandler
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients


class TableItem(private val table: String,
                private val category: String,
                private val cl: Clients,
                private val tableType: TableType = TableType.BASE_TABLE) : BaseTreeItem<String>(if (tableType == TableType.BASE_TABLE) TABLE_ICON else TABLE_VIEW_ICON) {
    init {
        this.value = table
        val handler = NavigatorMenuHandler.init(supportMenu)
        handler.getMenuCoroutine("打开", NavigatorMenuHandler.Companion.MenuType.OPEN, this::openTab)
    }

    suspend fun initField() {
        val list = SQLQuery.getClQuery(cl)
                .showTableField(category, table, currentClient.client).map {
                    TableFieldItem(it, cl)
                }
        this.children.addAll(list)
    }

    private suspend fun openTab() {
        val tab = TableTab(table, category, currentClient, tableType)
        MainTabPaneHandler.handler.addTabToPane(tab, fullPath) as TableTab
    }

    enum class TableType {
        BASE_TABLE,
        VIEW
    }
}
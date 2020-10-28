package cn.navigational.dbfx.controls.tree.table

import cn.navigational.dbfx.config.TABLE_ICON
import cn.navigational.dbfx.config.TABLE_VIEW_ICON
import cn.navigational.dbfx.controls.tab.TableTab
import cn.navigational.dbfx.controls.tree.TreeItemMenuHandler
import cn.navigational.dbfx.controls.tree.impl.ProgressTreeItem
import cn.navigational.dbfx.handler.MainTabPaneHandler
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.navigator.table.TableItem
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import javafx.event.ActionEvent
import javafx.scene.input.MouseEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TableTreeItem(
        private val uuid: String,
        private val table: String,
        private val category: String,
        private val tableType: TableItem.TableType = TableItem.TableType.BASE_TABLE) : ProgressTreeItem() {

    init {
        text = table
        val icon = if (tableType == TableItem.TableType.BASE_TABLE) TABLE_ICON else TABLE_VIEW_ICON
        prefixGra = SvgImageTranscoder.svgToImageView(icon)
        reListListener()
        initField()
    }

    private fun initField() {
        val optional = getSqlClient(uuid)
        if (optional.isEmpty) {
            return
        }
        val client = optional.get()
        GlobalScope.launch {
            loadStatus.set(true)
            try {
                val list = SQLQuery.getClQuery(client.cl)
                        .showTableField(category, table, client.client).map { TableFieldItem(it) }
                addChildren(list)
            } finally {
                loadStatus.set(false)
            }
        }

    }

    override fun onMouseClicked(event: MouseEvent) {
        super.onMouseClicked(event)
        if (event.clickCount > 1) {
            openTab()
        }
    }

    private fun openTab() {
        val path = fullPath
        val optional = getSqlClient(uuid)
        if (optional.isEmpty || MainTabPaneHandler.containPath(path)) {
            return
        }
        this.treeItem.isExpanded = !this.treeItem.isExpanded
        val tab = TableTab(table, category, optional.get(), tableType)
        GlobalScope.launch {
            MainTabPaneHandler.addTabToPane(tab, path) as TableTab
        }
    }

    override fun onAction(event: ActionEvent, action: TreeItemMenuHandler.MenuAction) {
    }

}
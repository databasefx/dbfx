package cn.navigational.dbfx.controls.tree.table

import cn.navigational.dbfx.config.TABLE_ICON
import cn.navigational.dbfx.config.TABLE_VIEW_ICON
import cn.navigational.dbfx.controls.tab.TableTab
import cn.navigational.dbfx.controls.tree.TreeItemMenuHandler
import cn.navigational.dbfx.controls.tree.impl.ProgressTreeItem
import cn.navigational.dbfx.handler.MainTabPaneHandler
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.view.ExportViewController
import javafx.event.ActionEvent
import javafx.scene.input.MouseEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TableTreeItem(
        private val uuid: String,
        private val table: String,
        private val category: String,
        private val tableType: TableType = TableType.BASE_TABLE) : ProgressTreeItem(
        SvgImageTranscoder.svgToImageView(if (tableType == TableType.BASE_TABLE)
            TABLE_ICON
        else
            TABLE_VIEW_ICON)) {

    init {
        text = table
        reListListener()
        initField()
        this.contextMenu.updateItem(ContextMenuAction.ADD,
                TreeItemMenuHandler.MenuAction.EDIT_TABLE,
                TreeItemMenuHandler.MenuAction.EXPORT_DATA_TO_FILE)
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
        if (optional.isEmpty || MainTabPaneHandler.containPath(path, switch = true)) {
            return
        }
        this.treeItem.isExpanded = !this.treeItem.isExpanded
        val tab = TableTab(table, category, optional.get(), tableType)
        GlobalScope.launch {
            MainTabPaneHandler.addTabToPane(tab, path) as TableTab
        }
    }

    override fun onAction(action: TreeItemMenuHandler.MenuAction) {
        if (action == TreeItemMenuHandler.MenuAction.EDIT_TABLE) {
            this.openTab()
        }
        if (action == TreeItemMenuHandler.MenuAction.EXPORT_DATA_TO_FILE) {
            ExportViewController(uuid, "$category.$table", ExportViewController.ExportTarget.TABLE).showStage()
        }
    }

    enum class TableType {
        BASE_TABLE,
        VIEW
    }

}
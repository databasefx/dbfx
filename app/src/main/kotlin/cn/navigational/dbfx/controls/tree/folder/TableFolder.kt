package cn.navigational.dbfx.controls.tree.folder

import cn.navigational.dbfx.config.FOLDER_ICON
import cn.navigational.dbfx.controls.tree.TreeItemMenuHandler
import cn.navigational.dbfx.controls.tree.table.TableTreeItem
import cn.navigational.dbfx.controls.tree.impl.ProgressTreeItem
import cn.navigational.dbfx.i18n.I18N
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import javafx.event.ActionEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TableFolder(private val category: String, uuid: String) : ProgressTreeItem() {
    init {
        text = I18N.getString("label.table")
        prefixGra = SvgImageTranscoder.svgToImageView(FOLDER_ICON)
        this.reListListener()
        this.initTable(uuid)
    }

    private fun initTable(uuid: String) {
        val optional = getSqlClient(uuid)
        if (optional.isEmpty) {
            return
        }
        val client = optional.get()
        val query = SQLQuery.getClQuery(client.cl)
        GlobalScope.launch {
            loadStatus.set(true)
            try {
                val list = query.showTable(category, client.client)
                addChildren(list.map { TableTreeItem(uuid, it, category) }.toList())
            } finally {
                loadStatus.set(false)
            }
        }
    }

    override fun onAction(action: TreeItemMenuHandler.MenuAction) {

    }
}
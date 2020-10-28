package cn.navigational.dbfx.controls.tree.folder

import cn.navigational.dbfx.config.FOLDER_ICON
import cn.navigational.dbfx.controls.tree.table.TableTreeItem
import cn.navigational.dbfx.controls.tree.impl.ProgressTreeItem
import cn.navigational.dbfx.i18n.I18N
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.navigator.table.TableItem
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewFolder(private val uuid: String, private val category: String) : ProgressTreeItem() {
    init {
        text = I18N.getString("label.view")
        prefixGra = SvgImageTranscoder.svgToImageView(FOLDER_ICON)
        this.reListListener()
        this.initView()
    }

    private fun initView() {
        val optional = getSqlClient(uuid)
        if (optional.isEmpty) {
            return
        }
        val client = optional.get()
        val query = SQLQuery.getClQuery(client.cl)
        GlobalScope.launch {
            loadStatus.set(true)
            try {
                val views = query.showView(category, client.client)
                val list = views.map {
                    TableTreeItem(uuid, it, category,
                            tableType = TableItem.TableType.VIEW)
                }.toList()
                addChildren(list)
            } finally {
                loadStatus.set(false)
            }
        }
    }
}
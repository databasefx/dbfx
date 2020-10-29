package cn.navigational.dbfx.controls.tree.folder

import cn.navigational.dbfx.config.FOLDER_ICON
import cn.navigational.dbfx.controls.tree.RoleItem
import cn.navigational.dbfx.controls.tree.TreeItemMenuHandler
import cn.navigational.dbfx.controls.tree.impl.ProgressTreeItem
import cn.navigational.dbfx.i18n.I18N
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.model.DbInfo
import cn.navigational.dbfx.model.SQLClient
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import javafx.event.ActionEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RoleFolder(uuid: String, category: String? = null) : ProgressTreeItem() {
    init {
        this.reListListener()
        getSqlClient(uuid).ifPresent {
            updateRoleInfo(it.dbInfo)
            initRole(it)
        }
    }

    private fun updateRoleInfo(info: DbInfo) {
        val txt = if (info.client == Clients.MYSQL) {
            "label.user"
        } else {
            "label.role"
        }
        this.text = I18N.getString(txt)
        this.prefixGra = SvgImageTranscoder.svgToImageView(FOLDER_ICON)
    }

    private fun initRole(client: SQLClient) {
        val query = SQLQuery.getClQuery(client.cl)
        GlobalScope.launch {
            loadStatus.set(true)
            try {
                val list = query.queryDbUser(client.client)
                val items = list.map { RoleItem(client.uuid, it) }.toList()
                addChildren(items)
            } finally {
                loadStatus.set(false)
            }
        }
    }

    override fun onAction(action: TreeItemMenuHandler.MenuAction?) {

    }
}
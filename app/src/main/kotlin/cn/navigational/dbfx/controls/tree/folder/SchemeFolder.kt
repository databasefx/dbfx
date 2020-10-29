package cn.navigational.dbfx.controls.tree.folder

import cn.navigational.dbfx.config.FOLDER_ICON
import cn.navigational.dbfx.controls.tree.TreeItemMenuHandler
import cn.navigational.dbfx.controls.tree.scheme.SchemeItem
import cn.navigational.dbfx.controls.tree.impl.ProgressTreeItem
import cn.navigational.dbfx.controls.tree.scheme.PgSchemeItem
import cn.navigational.dbfx.i18n.I18N
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.postgres.PgQuery
import cn.navigational.dbfx.model.SQLClient
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import javafx.event.ActionEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SchemeFolder() : ProgressTreeItem() {

    constructor(uuid: String, scheme: String? = null) : this() {
        this.reListListener()
        text = I18N.getString("label.database")
        prefixGra = SvgImageTranscoder.svgToImageView(FOLDER_ICON)
        this.getSqlClient(uuid).ifPresent { this.initScheme(it, scheme) }
    }

    private fun initScheme(client: SQLClient, scheme: String?) {
        GlobalScope.launch {
            loadStatus.set(true)
            try {
                if (client.cl == Clients.MYSQL) {
                    initMyScheme(client)
                } else if (client.cl == Clients.POSTGRESQL) {
                    initPgScheme(client, scheme!!)
                }
            } finally {
                loadStatus.set(false)
            }
        }
    }

    private suspend fun initMyScheme(client: SQLClient) {
        val query = SQLQuery.getClQuery(Clients.MYSQL)
        val list = query.showDatabase(client.client)
        this.addChildren(list.map { SchemeItem(it, client.uuid) }.toList())
    }

    private suspend fun initPgScheme(client: SQLClient, scheme: String) {
        this.text = I18N.getString("label.scheme")
        val query = SQLQuery.getClQuery(Clients.POSTGRESQL) as PgQuery
        val list = query.queryDbScheme(scheme, client.client).map {
            PgSchemeItem(client.uuid, it, scheme)
        }
        this.addChildren(list)
    }

    override fun onAction(action: TreeItemMenuHandler.MenuAction?) {

    }
}
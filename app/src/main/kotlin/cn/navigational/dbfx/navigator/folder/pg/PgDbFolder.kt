package cn.navigational.dbfx.navigator.folder.pg

import cn.navigational.dbfx.navigator.FolderItem
import cn.navigational.dbfx.navigator.scheme.PgDbItem
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.i18n.I18N
import cn.navigational.dbfx.kit.utils.StringUtils

class PgDbFolder : FolderItem() {
    init {
        value = I18N.getString("label.database")
    }

    override suspend fun initFolder() {
        if (this.children.isNotEmpty()) {
            this.children.clear()
        }
        val info = currentClient.dbInfo
        if (StringUtils.isNotEmpty(info.database)) {
            this.children.add(PgDbItem(info.database))
            return
        }
        val list = SQLQuery.getClQuery(Clients.POSTGRESQL).showDatabase(currentClient.client)
        for (s in list) {
            this.children.add(PgDbItem(s))
        }
    }
}
package cn.navigational.dbfx.navigator.user

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.config.PG_ICON
import cn.navigational.dbfx.config.PG_ROLE_ICON

class PgRoleItem(private val role: String) : BaseTreeItem<String>(PG_ROLE_ICON) {
    init {
        value = role
    }
}
package cn.navigational.dbfx.navigator.user

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.config.MYSQL_USER_ICON

class MyUserItem(private val username: String) : BaseTreeItem<String>() {
    init {
        value = username
        setIcon(MYSQL_USER_ICON)
    }
}
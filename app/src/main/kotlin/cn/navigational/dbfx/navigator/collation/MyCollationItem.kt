package cn.navigational.dbfx.navigator.collation

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.config.MYSQL_COLLATION_ICON

class MyCollationItem(private val collation: String) : BaseTreeItem<String>() {
    init {
        value = collation
        setIcon(MYSQL_COLLATION_ICON)
    }
}
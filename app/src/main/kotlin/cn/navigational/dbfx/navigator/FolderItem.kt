package cn.navigational.dbfx.navigator

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.config.SCHEME_ICON

abstract class FolderItem : BaseTreeItem<String>(SCHEME_ICON) {
    /**
     *
     * Inti folder content
     */
    abstract suspend fun initFolder()
}
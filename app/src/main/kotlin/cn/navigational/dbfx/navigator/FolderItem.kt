package cn.navigational.dbfx.navigator

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.config.FOLDER_ICON

abstract class FolderItem : BaseTreeItem<String>(FOLDER_ICON) {
    /**
     *
     * Inti folder content
     */
    abstract suspend fun initFolder()
}
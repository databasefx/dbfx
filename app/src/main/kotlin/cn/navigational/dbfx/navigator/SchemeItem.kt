package cn.navigational.dbfx.navigator

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.handler.NavigatorMenuHandler
import javafx.application.Platform

abstract class SchemeItem(icon: String, private val scheme: String) : BaseTreeItem<String>(icon) {
    init {
        val handler = NavigatorMenuHandler.init(supportMenu)
        val open = handler.getMenuCoroutine("打开", NavigatorMenuHandler.Companion.MenuType.OPEN, this::initData)
        val flush = handler.getMenuCoroutine("刷新", NavigatorMenuHandler.Companion.MenuType.FLUSH, this::initData, true)
        loadStatusProperty().addListener { _, _, n ->
            Platform.runLater {
                open.isDisable = n
                flush.isDisable = !n
            }
        }
    }

    /**
     *
     * Init current scheme data list,Open or flush call this method
     *
     */
    abstract suspend fun initData()
}
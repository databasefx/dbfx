package cn.navigational.dbfx.handler

import cn.navigational.dbfx.controls.AbstractBaseTab
import javafx.application.Platform
import javafx.scene.control.TabPane
import java.util.concurrent.ConcurrentHashMap

class MainTabPaneHandler private constructor() {
    private val tabPane: TabPane = TabPane()
    private val map: MutableMap<String, AbstractBaseTab> = ConcurrentHashMap()

    init {
        tabPane.styleClass.add("main-tab-pane")
    }

    fun getTabPane(): TabPane {
        return this.tabPane
    }

    suspend fun addTabToPane(tab: AbstractBaseTab, tabPath: String): AbstractBaseTab {
        val target = if (map.containsKey(tabPath)) {
            map[tabPath]!!
        } else {
            map[tabPath] = tab
            println("Success add $tabPath into TabPane")
            tab
        }
        Platform.runLater {
            if (tab == target) {
                this.tabPane.tabs.add(target)
            }
            this.tabPane.selectionModel.select(target)
        }
        target.init()
        return target
    }

    /**
     *
     * Close all tab
     *
     */
    suspend fun closeAllTab() {
        map.keys.forEach {
            val tab = map[it]!!
            tab.close()
            println("Tab[id=$it] close success!")
        }
        map.clear()
    }

    /**
     *
     * Single instance create MainTabPaneController
     *
     */
    companion object {
        val handler: MainTabPaneHandler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { MainTabPaneHandler() }
    }

}
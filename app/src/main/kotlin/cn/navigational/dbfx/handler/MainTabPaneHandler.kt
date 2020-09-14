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

    /**
     * Close single tab
     * @param path Tab path
     * @param calTabClose Whether call Tab close function
     */
    suspend fun closeTab(path: String, calTabClose: Boolean) {
        //If map not contain current path,not any!
        if (!this.map.containsKey(path)) {
            return
        }
        val tab = map[path]!!
        if (calTabClose) {
            tab.close()
        }
        this.map.remove(path)
        println("Tab[path=$path] close success!")
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
        target.setTabPath(tabPath)
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
            this.closeTab(it, true)
        }
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
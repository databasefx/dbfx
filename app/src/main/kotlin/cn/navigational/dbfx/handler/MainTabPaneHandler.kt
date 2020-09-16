package cn.navigational.dbfx.handler

import cn.navigational.dbfx.controls.AbstractBaseTab
import javafx.application.Platform
import javafx.scene.control.TabPane
import org.slf4j.LoggerFactory
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
        logger.debug("Tab[path={}] close success!", path)
    }

    suspend fun addTabToPane(tab: AbstractBaseTab, tabPath: String): AbstractBaseTab {
        val target = if (map.containsKey(tabPath)) {
            map[tabPath]!!
        } else {
            map[tabPath] = tab
            logger.debug("Success add {} into TabPane", tabPath)
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
        private val logger = LoggerFactory.getLogger(MainTabPaneHandler::class.java)
        /**
         * Single instance
         */
        val handler: MainTabPaneHandler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { MainTabPaneHandler() }
    }

}
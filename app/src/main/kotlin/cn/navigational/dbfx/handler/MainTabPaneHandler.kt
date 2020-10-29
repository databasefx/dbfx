package cn.navigational.dbfx.handler

import cn.navigational.dbfx.controls.AbstractBaseTab
import javafx.application.Platform
import javafx.scene.control.TabPane
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class MainTabPaneHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(MainTabPaneHandler::class.java)
        private val map: MutableMap<String, AbstractBaseTab> = ConcurrentHashMap()
        private val tabPane: TabPane = TabPane().also { it.styleClass.add("main-tab-pane") }

        fun getTabPane(): TabPane {
            return this.tabPane
        }

        /**
         * Close single tab
         * @param path Tab path
         */
        suspend fun closeTab(path: String) {
            //If map not contain current path,not any!
            if (!this.map.containsKey(path)) {
                return
            }
            val tab = this.map[path]!!.also { it.close() }
            this.map.remove(path)
            Platform.runLater { this.tabPane.tabs.remove(tab) }
            logger.debug("Tab[path={}] close success!", path)
        }

        /**
         *Close Tab by path prefix
         *
         * @param prefix Tab path prefix
         */
        suspend fun closeTabByPathPrefix(prefix: String) {
            val list = arrayListOf<String>()
            for (entry in this.map) {
                val key = entry.key
                if (key.startsWith(prefix)) {
                    list.add(key)
                }
            }
            for (path in list) {
                this.closeTab(path)
            }
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
                this.closeTab(it)
            }
        }

        fun containPath(path: String, switch: Boolean = false): Boolean {
            val contain = this.map.containsKey(path)
            if (contain && switch) {
                val tab = this.map[path]
                Platform.runLater { this.tabPane.selectionModel.select(tab) }
            }
            return contain
        }

    }

}
package cn.navigational.dbfx.controller

import cn.navigational.dbfx.controls.AbstractBaseTab
import javafx.application.Platform
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class MainTabPaneController private constructor() {
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
     * Single instance create MainTabPaneController
     *
     */
    companion object {
        private val controller: MainTabPaneController by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { MainTabPaneController() }

        @JvmName("getController1")
        fun getController(): MainTabPaneController {
            return controller
        }
    }

}
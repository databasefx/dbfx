package cn.navigational.dbfx.controller

import cn.navigational.dbfx.controls.AbstractBaseTab
import javafx.application.Platform
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import java.util.*

class MainTabPaneController private constructor() {
    private val tabPane: TabPane = TabPane()

    init {
        tabPane.styleClass.add("main-tab-pane")
    }

    fun getTabPane(): TabPane {
        return this.tabPane
    }

    suspend fun addTabToPane(tab: AbstractBaseTab): AbstractBaseTab {
        Objects.requireNonNull(tab)
        Platform.runLater {
            this.tabPane.tabs.add(tab)
        }
        tab.init()
        return tab
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
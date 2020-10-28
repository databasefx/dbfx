package cn.navigational.dbfx.controls

import cn.navigational.dbfx.handler.MainTabPaneHandler
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.control.Tab
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class AbstractBaseTab : Tab {
    constructor() : super() {
        setOnCloseRequest {
            val that = this
            GlobalScope.launch {
                that.close()
            }
        }
    }

    constructor(icon: String) : this() {
        this.graphic = SvgImageTranscoder.svgToImageView(icon)
    }

    /**
     * When tab instance after init data call that method
     */
    abstract suspend fun init()

    /**
     * When current tab request close call that method
     */
    open suspend fun close() {
        MainTabPaneHandler.closeTab(getTabPath())
    }

    /**
     *
     * Current Tab load status, if current tab doing load data that property value is true,
     * or else that property value is false.</p>
     *
     */
    private val loadStatus: BooleanProperty = SimpleBooleanProperty(false, "loadingStatus")

    fun getLoadStatus(): Boolean {
        return loadStatus.get()
    }

    fun getLoadStatusProperty(): BooleanProperty {
        return loadStatus
    }

    fun setLoadStatus(status: Boolean) {
        this.loadStatus.set(status)
    }

    /**
     *
     *     Note that the path must be unique and cannot be repeated,
     *     because the system will take the current path as a unique identifier.
     *     For any tab, for example, each time you open a tab,
     *     the system will detect whether the current path exists.
     *     If it does, the current current tab will be displayed.If not, a new tab with the
     *     current path as the key will be put into the system cache and the tab will be displayed
     *
     */
    private val tabPath: StringProperty = SimpleStringProperty(null, "tabPath")

    fun setTabPath(tabPath: String) {
        this.tabPath.set(tabPath)
    }

    fun getTabPath(): String {
        return tabPath.get()
    }

    fun getTabPathProperty(): StringProperty {
        return tabPath
    }
}
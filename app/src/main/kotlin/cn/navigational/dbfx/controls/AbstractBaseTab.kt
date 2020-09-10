package cn.navigational.dbfx.controls

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.control.Tab

abstract class AbstractBaseTab() : Tab() {

    /**
     * When tab instance after init data call that method
     */
    abstract suspend fun init()

    /**
     * When current tab request close call that method
     */
    abstract suspend fun close()

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
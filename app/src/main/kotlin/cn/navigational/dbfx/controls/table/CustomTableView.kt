package cn.navigational.dbfx.controls.table


import cn.navigational.dbfx.Launcher
import cn.navigational.dbfx.model.TableSetting
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.StringProperty
import javafx.collections.ObservableList
import javafx.scene.control.TableView

class CustomTableView : TableView<ObservableList<StringProperty>>() {
    /**
     * table setting property
     */
    private val tableSettingProperty: ObjectProperty<TableSetting> = SimpleObjectProperty(null, "tableSetting", Launcher.uiPreference.tableSetting)

    init {
        isEditable = true
        selectionModel.isCellSelectionEnabled = true
    }

    /**
     * Get current table setting
     */
    fun getTableSetting(): TableSetting {
        return this.tableSettingProperty.get()
    }

    /**
     * Set current table
     */
    fun setTableSetting(setting: TableSetting) {
        this.tableSettingProperty.set(setting)
    }

    /**
     *Get table setting property
     */
    fun getTableSettingProperty(): ObjectProperty<TableSetting> {
        return this.tableSettingProperty
    }
}
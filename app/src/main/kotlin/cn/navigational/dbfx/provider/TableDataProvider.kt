package cn.navigational.dbfx.provider

import cn.navigational.dbfx.kit.model.TableColumnMeta
import cn.navigational.dbfx.model.TableSetting
import javafx.beans.property.StringProperty
import javafx.collections.ObservableList

/**
 *
 *Table data provider
 *
 * @author yangkui
 * @since 1.0
 *
 */
interface TableDataProvider {
    /**
     * Get current table data total
     */
    suspend fun getDataTotal(): Long

    /**
     *Get current table column meta
     */
    suspend fun getColumnMeta(): List<TableColumnMeta>

    /**
     * Paging query current table data and transform to JavaFx collections
     * @param pageIndex current page index
     * @param pageSize current page size
     * @param setting current table setting
     */
    suspend fun getItems(pageIndex: Int, pageSize: Int, setting: TableSetting): List<ObservableList<StringProperty>>
}
package cn.navigational.dbfx.controls.tab

import cn.navigational.dbfx.convert.RowSetConvert
import cn.navigational.dbfx.config.TABLE_ICON
import cn.navigational.dbfx.config.TABLE_VIEW_ICON
import cn.navigational.dbfx.controller.TableViewController
import cn.navigational.dbfx.controls.AbstractBaseTab
import cn.navigational.dbfx.controls.tree.table.TableTreeItem
import cn.navigational.dbfx.model.SQLClient
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.model.TableColumnMeta
import cn.navigational.dbfx.model.TableSetting
import javafx.beans.property.StringProperty
import javafx.collections.ObservableList


class TableTab(
        private val table: String,
        private val category: String,
        private val client: SQLClient, tableType: TableTreeItem.TableType) : AbstractBaseTab(), TableViewController.TableDataProvider {

    private val controller: TableViewController = TableViewController(this)

    init {
        val host = client.dbInfo.host
        this.controller.load()
        this.text = "$table[$host]"
        this.content = controller.parent
        this.graphic = if (tableType == TableTreeItem.TableType.BASE_TABLE) {
            SvgImageTranscoder.svgToImageView(TABLE_ICON)
        } else {
            SvgImageTranscoder.svgToImageView(TABLE_VIEW_ICON)
        }
    }

    override suspend fun init() {}


    override suspend fun close() {
        this.controller.dispose()
        super.close()
    }

    override suspend fun getDataTotal(): Long {
        return SQLQuery.getClQuery(client.cl).queryTableTotal(category, table, client.client)

    }

    override suspend fun getColumnMeta(): List<TableColumnMeta> {
        return SQLQuery.getClQuery(client.cl).showTableField(category, table, client.client)
    }

    override suspend fun getItems(pageIndex: Int, pageSize: Int, setting: TableSetting): List<ObservableList<StringProperty>> {
        val sqlQuery = SQLQuery.getClQuery(client.cl)
        val list = sqlQuery.pageQuery(category, table, pageIndex, pageSize, client.client)
        return RowSetConvert.rowSetConvert(list, setting)
    }
}
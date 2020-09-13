package cn.navigational.dbfx.controls.tab

import cn.navigational.dbfx.RowSetConvert
import cn.navigational.dbfx.config.TABLE_ICON
import cn.navigational.dbfx.controller.TableViewController
import cn.navigational.dbfx.controls.AbstractBaseTab
import cn.navigational.dbfx.model.SQLClient
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.model.TableSetting
import javafx.beans.property.StringProperty
import javafx.collections.ObservableList


class TableTab(
        private val table: String,
        private val category: String,
        private val client: SQLClient) : AbstractBaseTab() {

    private val controller: TableViewController = TableViewController(this::loadData)

    init {
        val host = client.dbInfo.host
        this.controller.load()
        this.text = "$table[$host]"
        this.content = controller.parent
        this.graphic = SvgImageTranscoder.svgToImageView(TABLE_ICON)
    }

    override suspend fun init() {}


    private suspend fun loadData(pageIndex: Int, pageSize: Int,setting:TableSetting): List<ObservableList<StringProperty>> {
        val sqlQuery = SQLQuery.getClQuery(client.cl)
        val fields = sqlQuery.showTableField(category, table, client.client)
        val total = sqlQuery.queryTableTotal(category, table, client.client)
        controller.addColumn(fields)
        controller.setDataTotal(total)
        if (total <= 0) {
            return arrayListOf()
        }
        val list = sqlQuery.pageQuery(category, table, pageIndex, pageSize, client.client)
        val offset = (pageIndex - 1) * pageSize + 1
        return RowSetConvert.rowSetConvert(list, setting, offset)
    }

    override suspend fun close() {

    }
}
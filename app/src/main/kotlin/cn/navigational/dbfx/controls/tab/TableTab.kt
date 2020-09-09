package cn.navigational.dbfx.controls.tab

import cn.navigational.dbfx.config.TABLE_ICON
import cn.navigational.dbfx.controller.TableViewController
import cn.navigational.dbfx.controls.AbstractBaseTab
import cn.navigational.dbfx.model.SQLClient
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.kit.SQLQuery
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TableTab(private val table: String,
               private val category: String, private val client: SQLClient) : AbstractBaseTab() {

    private val controller: TableViewController = TableViewController()

    init {
        val host = client.dbInfo.host
        text = "$table[$host]"
        content = controller.parent
        controller.pageIndexProperty().addListener { _, _, _ ->
            loadData()
        }
        controller.pageSizeProperty().addListener { _, _, _ ->
            loadData()
        }
        graphic = SvgImageTranscoder.svgToImageView(TABLE_ICON)
    }

    override suspend fun init() {
        loadData()
        controller.flushHandler {
            loadData()
        }
    }


    private fun loadData() {
        val pageSize = controller.getPageSize()
        val pageIndex = controller.getPageIndex()
        GlobalScope.launch {
            val sqlQuery = SQLQuery.getClQuery(client.cl)
            val fields = sqlQuery.showTableField(category, table, client.client)
            val total = sqlQuery.queryTableTotal(category, table, client.client)
            controller.addColumn(fields)
            controller.setDataTotal(total)
            if (total <= 0) {
                return@launch
            }
            val list = sqlQuery.pageQuery(category, table, pageIndex, pageSize, client.client)
            Platform.runLater {
                controller.getTableItems().clear()
            }
            var offset = (pageIndex - 1) * pageSize + 1

            for (s in list) {
                val item = FXCollections.observableArrayList<StringProperty>()
                item.add(SimpleStringProperty(offset.toString()))
                for (value in s) {
                    item.add(SimpleStringProperty(value))
                }
                offset++
                Platform.runLater {
                    controller.getTableItems().add(item)
                }
            }
        }
    }

    override suspend fun close() {

    }
}
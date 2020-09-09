package cn.navigational.dbfx.controller

import cn.navigational.dbfx.Controller
import cn.navigational.dbfx.config.*
import cn.navigational.dbfx.controls.table.CustomTableColumn
import cn.navigational.dbfx.controls.table.CustomTableView
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.kit.model.TableColumnMeta
import cn.navigational.dbfx.kit.utils.NumberUtils
import cn.navigational.dbfx.utils.TableColumnUtils
import javafx.application.Platform
import javafx.beans.property.*
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane


class TableViewController() : Controller<Void, BorderPane>(TABLE_VIEW) {

    @FXML
    private lateinit var last: Button

    @FXML
    private lateinit var next: Button

    @FXML
    private lateinit var firstPage: Button

    @FXML
    private lateinit var lastPage: Button

    @FXML
    private lateinit var flush: Button

    @FXML
    private lateinit var sIcon: Label

    @FXML
    private lateinit var addRow: Button

    @FXML
    private lateinit var delRow: Button

    @FXML
    private lateinit var tableView: CustomTableView

    @FXML
    private lateinit var pageSelector: ChoiceBox<String>

    @FXML
    private lateinit var numIndicator: Label

    /**
     * Page size
     */
    private val pageSize: IntegerProperty = SimpleIntegerProperty(null, "pageSize", 10)

    /**
     * Page index
     */
    private val pageIndex: IntegerProperty = SimpleIntegerProperty(null, "pageIndex", 1)

    /**
     *
     * Data total property
     *
     */
    private val dataTotal: LongProperty = SimpleLongProperty(null, "dataTotal", 0)

    override fun onCreated(root: BorderPane?) {
        pageSelector.items.addAll("10", "100", "250", "500", "1000")
        pageSelector.selectionModel.select(0)
        last.graphic = SvgImageTranscoder.svgToImageView(LAST_ICON)
        next.graphic = SvgImageTranscoder.svgToImageView(NEXT_ICON)
        firstPage.graphic = SvgImageTranscoder.svgToImageView(FIRST_PAGE_ICON)
        lastPage.graphic = SvgImageTranscoder.svgToImageView(LAST_PAGE_ICON)
        flush.graphic = SvgImageTranscoder.svgToImageView(TABLE_FLUSH_ICON)
        sIcon.graphic = SvgImageTranscoder.svgToImageView(SEARCH_ICON)
        addRow.graphic = SvgImageTranscoder.svgToImageView(ADD_ROW_ICON)
        delRow.graphic = SvgImageTranscoder.svgToImageView(DEL_ROW_ICON)
        tableView.placeholder = Label("表中暂无数据")
        pageSelector.selectionModel.selectedItemProperty().addListener { _, _, n ->
            if (NumberUtils.isInteger(n)) {
                val pageSize = n.toInt()
                this.pageSize.value = pageSize
            }
        }
        next.setOnAction {
            this.pageIndex.value = ++this.pageIndex.value
        }
        last.setOnAction {
            val pageIndex = this.pageIndex.value
            if (pageIndex <= 1) {
                return@setOnAction
            }
            this.pageIndex.value = pageIndex - 1
        }
        firstPage.setOnAction {
            setPageIndex(1)
        }
        lastPage.setOnAction {
            val total = dataTotal.get()
            val size = pageSize.get()
            val index = (total + size - 1) / size
            setPageIndex(index.toInt())
        }
    }

    private fun updateIndicator() {
        val total = dataTotal.get()
        val index = getPageIndex()
        val size = getPageSize()
        val sOffset = (index - 1) * size
        val eOffset = sOffset + size
        val text = "$sOffset-$eOffset of $total"

        Platform.runLater {
            numIndicator.text = text
        }
    }

    fun addColumn(list: List<TableColumnMeta>) {
        val columns = tableView.columns
        if (columns.isEmpty()) {
            val cc = TableColumnUtils.createTableDataColumn(list)
            Platform.runLater {
                this.tableView.columns.addAll(cc)
            }
            return
        }
        val size = columns.size
        val offset = list.size + 1 - size
        for (i in list.indices) {
            val column = columns[i + 1] as CustomTableColumn
            column.updateTableColumn(list[i])
            if (i == size - 2) {
                break
            }
        }
        if (offset < 0) {
            Platform.runLater {
                tableView.columns.remove(size + offset, size)
            }
        }
        if (offset > 0) {
            val subList = list.subList(size - offset, list.size)
            val newColumns = subList.map { TableColumnUtils.createTableDataColumn(it, false) }
            Platform.runLater {
                tableView.columns.addAll(newColumns)
            }
        }
    }

    /**
     *
     * Register a flush handler
     *@param handler flush handler
     */
    fun flushHandler(handler: () -> Unit) {
        flush.setOnAction {
            handler()
        }
    }

    /**
     * Get table item list
     */
    fun getTableItems(): ObservableList<ObservableList<StringProperty>> {
        return tableView.items
    }

    /**
     *
     * Get current TableView all columns out of index column
     *
     */
    fun getColumns(): List<CustomTableColumn> {
        return tableView.columns
                .map { it as CustomTableColumn }
                .filter { !it.isIndexColumn() }
    }


    fun getPageSize(): Int {
        return pageSize.get()
    }

    fun pageSizeProperty(): IntegerProperty {
        return pageSize
    }

    fun setPageSize(pageSize: Int) {
        this.pageSize.set(pageSize)
    }


    fun getPageIndex(): Int {
        return pageIndex.get()
    }

    fun pageIndexProperty(): IntegerProperty {
        return pageIndex
    }

    fun setPageIndex(pageIndex: Int) {
        this.pageIndex.set(pageIndex)
    }


    fun getDataTotal(): Long {
        return dataTotal.get()
    }

    fun dataTotalProperty(): LongProperty {
        return dataTotal
    }

    fun setDataTotal(total: Long) {
        this.updateIndicator()
        this.dataTotal.set(total)
    }
}
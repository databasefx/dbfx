package cn.navigational.dbfx.controller

import cn.navigational.dbfx.Controller
import cn.navigational.dbfx.Launcher
import cn.navigational.dbfx.config.*
import cn.navigational.dbfx.controls.table.CustomTableColumn
import cn.navigational.dbfx.controls.table.CustomTableView
import cn.navigational.dbfx.dialog.TableSettingDialog
import cn.navigational.dbfx.io.flushUiPreference
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.kit.model.TableColumnMeta
import cn.navigational.dbfx.kit.utils.NumberUtils
import cn.navigational.dbfx.model.TableSetting
import cn.navigational.dbfx.utils.TableColumnUtils
import javafx.application.Platform
import javafx.beans.property.*
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TableViewController(private val provider: TableDataProvider) : Controller<Void, BorderPane>(TABLE_VIEW) {
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
    private lateinit var setting: Button

    @FXML
    private lateinit var tableView: CustomTableView

    @FXML
    private lateinit var pageSelector: ChoiceBox<String>

    @FXML
    private lateinit var numIndicator: Label

    /**
     * Page size
     */
    private val pageSizeProperty: IntegerProperty = SimpleIntegerProperty(null, "pageSize", 10)

    /**
     * Page index
     */
    private val pageIndexProperty: IntegerProperty = SimpleIntegerProperty(null, "pageIndex", 1)

    /**
     *
     * Data total property
     *
     */
    private val dataTotalProperty: LongProperty = SimpleLongProperty(null, "dataTotal", 0)


    override fun onCreated(root: BorderPane?) {
        tableView.tableSetting = Launcher.uiPreference.tableSetting
        this.tableView.placeholder = Label("表中暂无数据")
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
        setting.graphic = SvgImageTranscoder.svgToImageView(TABLE_SETTING_ICON)

        pageSelector.selectionModel.selectedItemProperty().addListener { _, _, n ->
            if (NumberUtils.isInteger(n)) {
                val pageSize = n.toInt()
                this.pageSizeProperty.value = pageSize
                load()
            }
        }
        next.setOnAction {
            this.pageIndexProperty.value = ++this.pageIndexProperty.value
            load()
        }
        last.setOnAction {
            val pageIndex = this.pageIndexProperty.value
            if (pageIndex <= 1) {
                return@setOnAction
            }
            this.pageIndexProperty.value = pageIndex - 1
            load()
        }
        firstPage.setOnAction {
            this.pageIndexProperty.set(1)
            load()
        }
        lastPage.setOnAction {
            val total = dataTotalProperty.get()
            val size = pageSizeProperty.get()
            val index = (total + size - 1) / size
            this.pageIndexProperty.set(index.toInt())
            load()
        }
        setting.setOnAction {
            val dialog = TableSettingDialog(tableView.tableSetting)
            val optional = dialog.showAndWait()
            val setting = optional.get()
            if (setting != tableView.tableSetting) {
                if (setting.isGlobal) {
                    Launcher.uiPreference.tableSetting = setting
                    flushUiPreference()
                }
                this.tableView.tableSetting = setting
                this.load()
            }
        }
        flush.setOnAction {
            load()
        }
    }

    /**
     * Load data use loadData function
     */
    fun load() {
        val pageSize = pageSizeProperty.get()
        val pageIndex = pageIndexProperty.get()
        val that = this
        GlobalScope.launch {
            val columns = provider.getColumnMeta()
            val total = provider.getDataTotal()
            val items = if (total > 0) {
                provider.getItems(pageIndex, pageSize, tableView.tableSetting)
            } else {
                arrayListOf()
            }
            that.updateColumn(columns)
            that.dataTotalProperty.set(total)
            that.updateIndicator()
            that.tableView.items.clear()
            if (items.isNotEmpty()) {
                that.tableView.items.addAll(items)
            }
        }
    }

    private fun updateIndicator() {
        val total = dataTotalProperty.get()
        val size = this.pageSizeProperty.get()
        val index = this.pageIndexProperty.get()
        val sOffset = (index - 1) * size
        val eOffset = sOffset + size
        val text = "$sOffset-$eOffset of $total"

        Platform.runLater {
            numIndicator.text = text
        }
    }

    private fun updateColumn(list: List<TableColumnMeta>) {
        val columns = tableView.columns
        //Only have index column == empty
        if (columns.size == 1) {
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
            val newColumns = subList.map { TableColumnUtils.createTableDataColumn(it) }
            Platform.runLater {
                tableView.columns.addAll(newColumns)
            }
        }
    }

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
}
package cn.navigational.dbfx.controls.table

import cn.navigational.dbfx.config.*
import cn.navigational.dbfx.kit.enums.DataType
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.kit.model.TableColumnMeta
import javafx.application.Platform
import javafx.beans.property.*
import javafx.collections.ObservableList
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.text.Text

class CustomTableColumn : TableColumn<ObservableList<StringProperty>, String> {
    companion object {
        private val numberImage = SvgImageTranscoder.svgToImage(NUMBER_FIELD)
        private val strImage = SvgImageTranscoder.svgToImage(STRING_FIELD)
        private val dateTimeImage = SvgImageTranscoder.svgToImage(DATE_TIME_FIELD)

        fun getFieldImage(dataType: DataType): Image {
            return when (dataType) {
                DataType.NUMBER -> numberImage
                DataType.DATE -> dateTimeImage
                else -> strImage
            }
        }
    }

    /**
     * Current column is index column?
     */
    private val indexColumn: BooleanProperty = SimpleBooleanProperty(false, "indexColumn")


    /**
     *When current column as data column use that constructor
     */
    private val tableColumnMeta: ObjectProperty<TableColumnMeta> = SimpleObjectProperty(null, "tableColumnMeta")

    constructor(column: TableColumnMeta) : this(false) {
        this.updateTableColumn(column)
    }

    constructor(indexColumn: Boolean) : super() {
        sortNode = SvgImageTranscoder.svgToImageView(ASC_ICON)
        sortTypeProperty().addListener { _, _, n ->
            sortNode = if (n == SortType.ASCENDING) {
                SvgImageTranscoder.svgToImageView(ASC_ICON)
            } else {
                SvgImageTranscoder.svgToImageView(DES_ICON)
            }
        }
        if (indexColumn) {
            isSortable = false
            this.styleClass.add("index-column")
            setCellFactory { IndexTableCell() }
        } else {
            this.styleClass.add("data-column")
            setCellFactory { DataTableCell() }
        }
        this.setIndexColumn(indexColumn)
    }

    fun updateTableColumn(column: TableColumnMeta) {
        val colName = column.colName
        var tip = "$colName:${column.type}"
        if (column.length != null) {
            tip += "(${column.length})"
        }
        this.tableColumnMeta.set(column)
        val graphic = Label()
        graphic.tooltip = Tooltip(tip)
        graphic.graphic = ImageView(getFieldImage(column.dataType))
        Platform.runLater {
            this.text = colName
            this.graphic = graphic
        }
    }

    fun isIndexColumn(): Boolean {
        return indexColumn.get()
    }

    fun indexColumnProperty(): BooleanProperty? {
        return indexColumn
    }

    fun setIndexColumn(indexColumn: Boolean) {
        this.indexColumn.set(indexColumn)
    }

    /**
     *
     * Get current column for TableColumnMeta
     *
     */
    fun getTableColumnMeta(): TableColumnMeta {
        if (indexColumn.value) {
            throw RuntimeException("Current column is index column,so not have TableColumnMeta!")
        }
        return tableColumnMeta.get()
    }
}
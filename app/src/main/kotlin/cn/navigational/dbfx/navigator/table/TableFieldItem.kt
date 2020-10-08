package cn.navigational.dbfx.navigator.table

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.controls.table.CustomTableColumn
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.model.TableColumnMeta
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox

class TableFieldItem(columnMeta: TableColumnMeta, cl: Clients) : BaseTreeItem<String>() {
    init {
        val hBox = HBox()
        val label = Label(columnMeta.colName)
        val note = Label(" ${columnMeta.type}")
        val icon = ImageView(CustomTableColumn.getFieldImage(columnMeta.dataType))
        hBox.children.addAll(icon, label, note)
        note.styleClass.add("field-note")
        hBox.styleClass.add("table-field-item")
        graphic = hBox
    }
}
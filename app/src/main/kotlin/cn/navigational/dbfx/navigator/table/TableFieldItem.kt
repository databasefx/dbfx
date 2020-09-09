package cn.navigational.dbfx.navigator.table

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.controls.table.CustomTableColumn
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.model.TableColumnMeta
import javafx.scene.image.ImageView

class TableFieldItem(columnMeta: TableColumnMeta, cl: Clients) : BaseTreeItem<String>() {
    init {
        value = "${columnMeta.colName}${if (columnMeta.comment == "") "" else '(' + columnMeta.comment + ')'}"
        graphic = ImageView(CustomTableColumn.getFieldImage(columnMeta.dataType))
    }
}
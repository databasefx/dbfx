package cn.navigational.dbfx.controls.tree.table

import cn.navigational.dbfx.controls.table.CustomTableColumn
import cn.navigational.dbfx.controls.tree.AbstractBaseTreeItem
import cn.navigational.dbfx.controls.tree.TreeItemMenuHandler
import cn.navigational.dbfx.kit.model.TableColumnMeta
import javafx.event.ActionEvent
import javafx.scene.image.ImageView

class TableFieldItem(tableColumnMeta: TableColumnMeta) : AbstractBaseTreeItem() {
    init {
        this.text = tableColumnMeta.colName
        this.setSuffixTx(tableColumnMeta.type)
        this.prefixGra = ImageView(CustomTableColumn.getFieldImage(tableColumnMeta))
    }

    override fun onAction(event: ActionEvent, action: TreeItemMenuHandler.MenuAction) {
    }
}
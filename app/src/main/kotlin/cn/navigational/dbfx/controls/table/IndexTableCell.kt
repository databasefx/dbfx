package cn.navigational.dbfx.controls.table

import javafx.beans.property.StringProperty
import javafx.collections.ObservableList
import javafx.scene.control.TableCell

/**
 *
 *Table index column cell
 * @author yangkui
 * @since 1.0
 */
class IndexTableCell : TableCell<ObservableList<StringProperty>, String>() {

    override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty) {
            text = null
            return
        }
        text = item
    }
}
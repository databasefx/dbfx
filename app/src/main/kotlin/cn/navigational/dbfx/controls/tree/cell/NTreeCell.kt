package cn.navigational.dbfx.controls.tree.cell

import javafx.scene.control.ContextMenu
import javafx.scene.control.TreeCell

/**
 *
 * Customer {@link TreeCell}
 *
 * @author yangkui
 * @since 1.0
 */
class NTreeCell : TreeCell<String>() {
    override fun updateItem(item: String?, empty: Boolean) {
        if (empty || treeItem == null) {
            this.text = null
            this.graphic = null
        } else {
            graphic = treeItem.graphic
        }
        super.updateItem(item, empty)
    }
}
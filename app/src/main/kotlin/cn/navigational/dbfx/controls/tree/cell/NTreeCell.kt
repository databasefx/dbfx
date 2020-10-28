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
    init {
        this.contextMenu = ContextMenu()
    }

    override fun updateItem(item: String?, empty: Boolean) {
        if (empty || treeItem == null) {
            this.text = null
            this.graphic = null
        } else {
            text = item
            graphic = treeItem.graphic
            this.contextMenu.items.clear()
            //this.contextMenu.items.addAll((treeItem as BaseTreeItem).supportMenu)
        }
        super.updateItem(item, empty)
    }
}
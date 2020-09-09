package cn.navigational.dbfx.controls.tree

import cn.navigational.dbfx.BaseTreeItem
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
        if (empty || item == null || treeItem == null) {
            this.text = null
            this.graphic = null
            this.contextMenu = null
        } else {
            text = item
            graphic = treeItem.graphic
            val it = treeItem as BaseTreeItem
            if (it.supportMenu.isNotEmpty()) {
                contextMenu = ContextMenu()
                contextMenu.items.addAll((treeItem as BaseTreeItem).supportMenu)
            } else {
                contextMenu = null
            }
        }
        super.updateItem(item, empty)
    }
}
package cn.navigational.dbfx.controls.tree.scheme

import cn.navigational.dbfx.config.PG_DB_ICON
import cn.navigational.dbfx.controls.tree.TreeItemMenuHandler
import cn.navigational.dbfx.controls.tree.folder.TableFolder
import cn.navigational.dbfx.controls.tree.folder.ViewFolder
import cn.navigational.dbfx.controls.tree.impl.ProgressTreeItem
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import javafx.event.ActionEvent
import javafx.scene.input.MouseEvent

class PgSchemeItem(private val uuid: String, private val scheme: String, private val category: String) : ProgressTreeItem() {
    init {
        this.text = scheme
        this.prefixGra = SvgImageTranscoder.svgToImageView(PG_DB_ICON)
    }

    private fun initScheme() {
        val s = "$category.$scheme"
        val tableFolder = TableFolder(s, uuid)
        val viewFolder = ViewFolder(uuid, s)
        this.addChildren(tableFolder, viewFolder)
    }

    override fun onMouseClicked(event: MouseEvent) {
        if (event.clickCount > 1 && this.treeItem.children.isEmpty()) {
            initScheme()
        }
    }

    override fun onAction(event: ActionEvent, action: TreeItemMenuHandler.MenuAction) {
    }
}
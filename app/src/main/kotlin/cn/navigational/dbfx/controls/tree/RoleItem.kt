package cn.navigational.dbfx.controls.tree

import cn.navigational.dbfx.config.MYSQL_USER_ICON
import cn.navigational.dbfx.config.PG_ROLE_ICON
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import javafx.event.ActionEvent

class RoleItem(uuid: String, name: String) : AbstractBaseTreeItem() {
    init {
        this.text = name
        getSqlClient(uuid).ifPresent {
            val str = if (it.cl == Clients.POSTGRESQL) {
                PG_ROLE_ICON
            } else {
                MYSQL_USER_ICON
            }
            this.prefixGra = SvgImageTranscoder.svgToImageView(str)
        }
    }

    override fun onAction(action: TreeItemMenuHandler.MenuAction?) {
    }
}
package cn.navigational.dbfx.navigator

import cn.navigational.dbfx.config.*
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.utils.AlertUtils
import javafx.scene.control.MenuItem
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NavigatorMenuHandler(private val list: MutableList<MenuItem>) {

    private fun getMenuItem(text: String, menu: MenuType): MenuItem {
        val item = MenuItem(text)
        this.list.add(item)
        val image = when (menu) {
            MenuType.FLUSH -> flushImage
            MenuType.OPEN_CONNECT -> conImage
            MenuType.DIS_CONNECT -> disConImage
            MenuType.SQL_TERMINAL -> sqlTerImage
            MenuType.OPEN -> openImage
            else -> editImage
        }
        item.graphic = ImageView(image)
        return item
    }

    fun getMenuItem(text: String, menu: MenuType, handler: suspend () -> Unit, disabled: Boolean = false): MenuItem {
        val item = getMenuItem(text, menu)
        item.isDisable = disabled
        item.setOnAction {
            GlobalScope.launch {
                //Execute handler and catch exception
                try {
                    handler()
                } catch (e: Exception) {
                    AlertUtils.showExDialog("操作失败", e)
                }
            }
        }
        return item
    }

    companion object {

        private val openImage: Image = SvgImageTranscoder.svgToImage(OPEN_ICON)
        private val editImage: Image = SvgImageTranscoder.svgToImage(EDIT_ICON)
        private val flushImage: Image = SvgImageTranscoder.svgToImage(FLUSH_ICON)
        private val conImage: Image = SvgImageTranscoder.svgToImage(CON_MINI_ICON)
        private val disConImage: Image = SvgImageTranscoder.svgToImage(DIS_CON_ICON)
        private val sqlTerImage: Image = SvgImageTranscoder.svgToImage(SQL_TER_ICON)


        enum class MenuType {
            FLUSH,
            OPEN_CONNECT,
            DIS_CONNECT,
            SQL_TERMINAL,
            EDIT_CONNECT,
            OPEN
        }

        fun init(list: MutableList<MenuItem>): NavigatorMenuHandler {
            return NavigatorMenuHandler(list)
        }
    }
}
package cn.navigational.dbfx.handler

import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.dialog.ProgressDialog
import javafx.application.Platform
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.lang.Exception
import kotlin.math.log

/**
 *
 *
 * Close app occur Resource
 *
 */
private val logger = LoggerFactory.getLogger("AppExitHandler")

fun closeAppOccurResource() {
    val dialog = ProgressDialog()
    dialog.show()
    dialog.updateText(ProgressDialog.TextType.TIP, "释放资源")
    GlobalScope.launch {
        try {
            dialog.updateText(ProgressDialog.TextType.DES, "正在关闭所Tab...")
            MainTabPaneHandler.closeAllTab()
            dialog.updateText(ProgressDialog.TextType.DES, "正在关闭所有连接...")
            SQLClientManager.manager.closeAllClient()
        } catch (e: Exception) {
            logger.debug("App exit close resource failed cause:[${e.message}]")
            logger.error("Close resource happen error.", e)
        }
        dialog.closeDialog()
        //Exit program
        Platform.exit()
    }
}
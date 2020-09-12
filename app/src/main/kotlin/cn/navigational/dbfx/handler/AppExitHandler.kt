package cn.navigational.dbfx.handler

import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.dialog.ProgressDialog
import javafx.application.Platform
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 *
 *
 * Close app occur Resource
 *
 */
fun closeAppOccurResource() {
    val dialog = ProgressDialog()
    dialog.show()
    dialog.updateText(ProgressDialog.TextType.TIP, "释放资源")
    GlobalScope.launch {
        try {
            dialog.updateText(ProgressDialog.TextType.DES, "正在关闭所Tab...")
            MainTabPaneHandler.handler.closeAllTab()
            dialog.updateText(ProgressDialog.TextType.DES, "正在关闭所有连接...")
            SQLClientManager.manager.closeAllClient()
        } catch (e: Exception) {
            println("App exit close resource failed cause:[${e.message}]")
            e.printStackTrace()
        }
        dialog.closeDialog()
        //Exit program
        Platform.exit()
    }
}
package cn.navigational.dbfx.controller

import cn.navigational.dbfx.config.B_N_LOG_PANE
import cn.navigational.dbfx.io.APP_LOG_PATH
import cn.navigational.dbfx.io.countLogLineNumber
import cn.navigational.dbfx.io.getFileLastTime
import cn.navigational.dbfx.io.readFixLineLog
import cn.navigational.dbfx.kit.i18n.I18N
import cn.navigational.dbfx.kit.utils.VertxUtils
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.MenuItem
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class LogController : BottomNavigationExpandPaneController.ExpandPaneProvider<Void, BorderPane>(B_N_LOG_PANE) {

    private var total = 0
    private var rowIndex = 1
    private val pageSize = 100
    private val timerId: Long

    private var path = "${APP_LOG_PATH}dbfx.log"
    private var lastModifyTime = 0L


    @FXML
    private lateinit var textArea: TextArea


    init {
        loadLog()
        timerId = VertxUtils.getVertx().setPeriodic(500) {
            val file = File(path)
            if (!file.exists()) {
                return@setPeriodic
            }
            val temp = file.lastModified()
            if (temp != lastModifyTime) {
                loadLog()
            }
        }
    }

    private fun loadLog() {
        val that = this
        GlobalScope.launch {
            total = countLogLineNumber(path)
            while (true) {
                if (rowIndex > total) {
                    rowIndex = total
                    break
                }
                //读取指定行的数据
                val content = readFixLineLog(path, rowIndex, rowIndex + pageSize)
                rowIndex += pageSize
                Platform.runLater {
                    that.textArea.appendText(content)
                }
            }
            that.lastModifyTime = getFileLastTime(path)
        }
    }

    override fun getSetting(): MutableList<MenuItem> {
        return arrayListOf()
    }

    override fun getTitle(): String {
        return I18N.getString("tool.bar.log")
    }

    override fun close() {
        VertxUtils.getVertx().cancelTimer(timerId)
    }
}
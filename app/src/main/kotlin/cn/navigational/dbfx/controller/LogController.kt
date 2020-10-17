package cn.navigational.dbfx.controller

import cn.navigational.dbfx.config.B_N_LOG_PANE
import cn.navigational.dbfx.editor.EditorPlatform.APP_LOG_PATH
import cn.navigational.dbfx.i18n.I18N
import cn.navigational.dbfx.kit.utils.OssUtils
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.MenuItem
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import java.awt.Desktop
import java.io.File
import java.io.OutputStream
import javax.swing.SwingUtilities

class LogController private constructor() : BottomNavigationExpandPaneAbstractFxmlController.ExpandPaneProvider<BorderPane>(B_N_LOG_PANE) {
    //whether listener listener log output stream
    var listenerLog: Boolean = true
    private val output: OutputStream = LogConsole()
    private val clear: MenuItem = MenuItem(I18N.getString("expand.pane.setting.action.clear"))
    private val openLogDir: MenuItem = MenuItem(I18N.getString("expand.pane.setting.action.open.log.dir"))

    @FXML
    private lateinit var textArea: TextArea


    init {
        this.clear.setOnAction {
            this.textArea.clear()
        }
        this.openLogDir.setOnAction {
            OssUtils.openDirOrFileUseFileSystem(APP_LOG_PATH)
        }
    }

    override fun getSetting(): MutableList<MenuItem> {
        return arrayListOf(clear, openLogDir)
    }

    override fun getTitle(): String {
        return I18N.getString("tool.bar.log")
    }

    override fun close() {}

    fun getOutput(): OutputStream {
        return this.output
    }

    inner class LogConsole : OutputStream() {
        override fun write(b: Int) {

        }

        override fun write(b: ByteArray) {
            if (!listenerLog) {
                return
            }
            val logTxt = String(b)
            Platform.runLater {
                textArea.appendText(logTxt)
            }
        }

        override fun write(b: ByteArray, off: Int, len: Int) {
            super.write(b, off, len)
        }
    }

    companion object {
        val logController: LogController by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { LogController() }
    }
}
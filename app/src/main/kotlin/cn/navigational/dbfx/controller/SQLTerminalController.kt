package cn.navigational.dbfx.controller

import cn.navigational.dbfx.AbstractFxmlController
import cn.navigational.dbfx.config.SQL_TERMINAL_PAGE
import cn.navigational.dbfx.config.T_EXE_RESULT_ICON
import cn.navigational.dbfx.config.T_INFO_ICON
import cn.navigational.dbfx.config.T_START_ICON
import cn.navigational.dbfx.controls.editor.SQLAutoCompletePopup
import cn.navigational.dbfx.controls.table.CustomTableView
import cn.navigational.dbfx.convert.RowSetConvert
import cn.navigational.dbfx.kit.SQLExecutor
import cn.navigational.dbfx.model.SQLClient
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.utils.AppSettings
import cn.navigational.dbfx.utils.TableColumnUtils
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.geometry.Point2D
import javafx.scene.control.*
import javafx.scene.input.InputMethodRequests
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import java.lang.NullPointerException


class SQLTerminalController(val cl: SQLClient) : AbstractFxmlController<SplitPane>(SQL_TERMINAL_PAGE) {
    @FXML
    private lateinit var info: Tab

    @FXML
    private lateinit var execute: Button

    @FXML
    private lateinit var exeResult: Tab

    @FXML
    private lateinit var codeArea: CodeArea

    @FXML
    private lateinit var exeInfo: TextArea

    @FXML
    private lateinit var tabPane: TabPane

    @FXML
    private lateinit var tableView: CustomTableView

    private val autoCompletePopup: SQLAutoCompletePopup


    init {
        this.tabPane.tabs.remove(exeResult)
        this.autoCompletePopup = SQLAutoCompletePopup(cl.cl, codeArea);
        codeArea.paragraphGraphicFactory = LineNumberFactory.get(codeArea)
        info.graphic = SvgImageTranscoder.svgToImageView(T_INFO_ICON)
        execute.graphic = SvgImageTranscoder.svgToImageView(T_START_ICON)
        exeResult.graphic = SvgImageTranscoder.svgToImageView(T_EXE_RESULT_ICON)
        codeArea.setOnInputMethodTextChanged {
            if (it.committed != "") {
                codeArea.replaceSelection("")
                codeArea.insertText(codeArea.caretPosition, it.committed)
            }
        }
        execute.setOnAction {
            val sql = codeArea.text
            if (sql.trim() == "") {
                return@setOnAction
            }
            tabPane.selectionModel.select(info)
            this.exeInfo.clear()
            this.exeInfo.appendText("> $sql\n")
            GlobalScope.launch { execute(sql) }
        }
        codeArea.inputMethodRequests = CodeAreaInputRequest()
    }

    private suspend fun execute(rowTxt: String) {
        //replace all wrapper symbol
        val sql = rowTxt.replace("\n", "")
        val start = System.currentTimeMillis()
        val status = try {
            val res = SQLExecutor.executeSql(sql, cl.cl, cl.client)
            val columns = res.columnsNames()
            if (columns != null && columns.isNotEmpty()) {
                queryHandler(res)
            } else {
                updateHandler(res)
            }
        } catch (e: Exception) {
            "FAILED:${e.message}"
        }
        val end = System.currentTimeMillis()
        Platform.runLater {
            this.exeInfo.appendText("> $status\n")
            this.exeInfo.appendText("> ${(end - start) / 1000.0}s\n")
        }
    }

    private fun queryHandler(res: RowSet<Row>): String {
        val app = AppSettings.getAppSettings();
        TableColumnUtils.createTableColumns(tableView, res.columnsNames())
        RowSetConvert.rowSetConvert(tableView, res, app.tableSetting)
        Platform.runLater {
            if (!tabPane.tabs.contains(exeResult)) {
                tabPane.tabs.add(exeResult)
            }
            tabPane.selectionModel.select(exeResult)
        }
        return "OK"
    }

    private fun updateHandler(res: RowSet<Row>): String {
        val updated = res.rowCount()
        Platform.runLater {
            this.tabPane.tabs.remove(exeResult)
        }
        return "Affected rows:$updated"
    }

    private inner class CodeAreaInputRequest : InputMethodRequests {
        override fun getTextLocation(offset: Int): Point2D {
            val caretPositionBounds = codeArea.caretBounds
            if (caretPositionBounds.isPresent) {
                val bounds = caretPositionBounds.get()
                return Point2D(bounds.maxX - 5, bounds.maxY)
            }
            throw NullPointerException()
        }

        override fun getLocationOffset(x: Int, y: Int): Int {
            return 0
        }

        override fun cancelLatestCommittedText() {
        }

        override fun getSelectedText(): String {
            return ""
        }

    }

    override fun dispose() {
        this.autoCompletePopup.dispose()
    }
}
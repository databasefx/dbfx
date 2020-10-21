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
import cn.navigational.dbfx.kit.SqlParseHelper
import cn.navigational.dbfx.model.SQLClient
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.utils.AppSettings
import cn.navigational.dbfx.utils.TableColumnUtils
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
    private lateinit var statement: Label

    @FXML
    private lateinit var exeStatus: Label

    @FXML
    private lateinit var exeTime: Label

    @FXML
    private lateinit var tableView: CustomTableView

    @FXML
    private lateinit var tabPane: TabPane

    private val autoCompletePopup: SQLAutoCompletePopup


    init {
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
            this.statement.text = "> $sql"
            GlobalScope.launch { execute(sql) }
        }
        codeArea.inputMethodRequests = CodeAreaInputRequest()
    }

    private suspend fun execute(sql: String) {
        val start = System.currentTimeMillis()
        var status = "ok"
        var query: Boolean
        try {
            query = SqlParseHelper.selectStatement(sql)
            val res = SQLExecutor.executeSql(sql, cl.cl, cl.client)
            if (query) {
                val app = AppSettings.getAppSettings();
                TableColumnUtils.createTableColumns(tableView, res.columnsNames())
                RowSetConvert.rowSetConvert(tableView, res, app.tableSetting)
            }
        } catch (e: Exception) {
            status = "failed:${e.message}"
            query = false
        }
        val end = System.currentTimeMillis()
        Platform.runLater {
            this.exeStatus.text = "> $status"
            this.exeTime.text = "> ${(end - start) / 1000.0}s"
            if (query) {
                tabPane.selectionModel.select(exeResult)
            }
        }
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
package cn.navigational.dbfx.controller

import cn.navigational.dbfx.AbstractFxmlController
import cn.navigational.dbfx.config.SQL_TERMINAL_PAGE
import cn.navigational.dbfx.config.T_EXE_RESULT_ICON
import cn.navigational.dbfx.config.T_INFO_ICON
import cn.navigational.dbfx.config.T_START_ICON
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import impl.org.controlsfx.skin.AutoCompletePopup
import javafx.fxml.FXML
import javafx.geometry.Point2D
import javafx.scene.control.*
import javafx.scene.input.InputMethodRequests
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import java.lang.NullPointerException
import java.lang.RuntimeException


class SQLTerminalController : AbstractFxmlController<SplitPane>(SQL_TERMINAL_PAGE) {
    @FXML
    private lateinit var info: Tab

    @FXML
    private lateinit var execute: Button

    @FXML
    private lateinit var exeResult: Tab

    @FXML
    private lateinit var codeArea: CodeArea

    private val autoCompletePopup: AutoCompletePopup<String> = AutoCompletePopup()

    private val keywords: Array<String> = arrayOf("SELECT", "FROM", "WHERE", "JOIN", "ON", "LEFT")

    init {
        autoCompletePopup.isAutoFix = true
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
        codeArea.textProperty().addListener { _, _, newValue ->
            this.findClosestWord(newValue)
        }
        codeArea.inputMethodRequests = CodeAreaInputRequest()
    }

    private fun findClosestWord(text: String) {
        val pos = codeArea.caretPosition
        val str = text.substring(0, pos)
        var index = str.lastIndexOf(" ")
        if (index == -1) {
            index = str.lastIndexOf("\n")
        }
        if (index == -1) {
            index = 0
        }
        this.autoCompletePopup.suggestions.clear()
        val keyword = str.substring(index, pos).toUpperCase().trim()
        if (keyword == "") {
            autoCompletePopup.hide()
            return
        }
        keywords.forEach {
            if (it.startsWith(keyword)) {
                this.autoCompletePopup.suggestions.add(it)
            }
        }
        if (this.autoCompletePopup.suggestions.isNotEmpty()) {
//            val rowIndex = codeArea.caretPosition
//            val columnIndex = codeArea.caretColumn
//            println("row index:$rowIndex,column index:$columnIndex")
            //popup window
            if (!autoCompletePopup.isShowing) {
                autoCompletePopup.show(codeArea)
            }
            //fix popup position
        } else {
            autoCompletePopup.hide()
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


}
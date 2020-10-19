package cn.navigational.dbfx.controller

import cn.navigational.dbfx.AbstractFxmlController
import cn.navigational.dbfx.config.SQL_TERMINAL_PAGE
import cn.navigational.dbfx.config.T_EXE_RESULT_ICON
import cn.navigational.dbfx.config.T_INFO_ICON
import cn.navigational.dbfx.config.T_START_ICON
import cn.navigational.dbfx.controls.editor.SQLAutoCompletePopup
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import javafx.fxml.FXML
import javafx.geometry.Point2D
import javafx.scene.control.*
import javafx.scene.input.InputMethodRequests
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import java.lang.NullPointerException


class SQLTerminalController(cl: Clients) : AbstractFxmlController<SplitPane>(SQL_TERMINAL_PAGE) {
    @FXML
    private lateinit var info: Tab

    @FXML
    private lateinit var execute: Button

    @FXML
    private lateinit var exeResult: Tab

    @FXML
    private lateinit var codeArea: CodeArea

    private val autoCompletePopup: SQLAutoCompletePopup


    init {
        this.autoCompletePopup = SQLAutoCompletePopup(cl, codeArea);
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

        codeArea.inputMethodRequests = CodeAreaInputRequest()
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
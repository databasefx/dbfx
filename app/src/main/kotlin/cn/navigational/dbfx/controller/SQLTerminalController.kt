package cn.navigational.dbfx.controller

import cn.navigational.dbfx.AbstractFxmlController
import cn.navigational.dbfx.config.SQL_TERMINAL_PAGE
import cn.navigational.dbfx.config.T_EXE_RESULT_ICON
import cn.navigational.dbfx.config.T_INFO_ICON
import cn.navigational.dbfx.config.T_START_ICON
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import javafx.fxml.FXML
import javafx.scene.control.*
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory


class SQLTerminalController : AbstractFxmlController<SplitPane>(SQL_TERMINAL_PAGE) {
    @FXML
    private lateinit var info: Tab

    @FXML
    private lateinit var execute: Button

    @FXML
    private lateinit var exeResult: Tab

    @FXML
    private lateinit var codeArea: CodeArea

    init {
        codeArea.paragraphGraphicFactory = LineNumberFactory.get(codeArea)
        info.graphic = SvgImageTranscoder.svgToImageView(T_INFO_ICON)
        execute.graphic = SvgImageTranscoder.svgToImageView(T_START_ICON)
        exeResult.graphic = SvgImageTranscoder.svgToImageView(T_EXE_RESULT_ICON)
    }


}
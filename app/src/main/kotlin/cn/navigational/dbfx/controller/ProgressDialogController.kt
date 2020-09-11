package cn.navigational.dbfx.controller

import cn.navigational.dbfx.Controller
import cn.navigational.dbfx.config.APP_STYLE
import cn.navigational.dbfx.config.MINI_ICON
import cn.navigational.dbfx.config.PROGRESS_DIALOG_PANE
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Dialog
import javafx.scene.control.DialogPane
import javafx.scene.control.ProgressBar
import javafx.scene.layout.BorderPane
import javafx.stage.StageStyle

class ProgressDialogController : Controller<Void, BorderPane>(PROGRESS_DIALOG_PANE) {
    @FXML
    private lateinit var mini: Button

    @FXML
    private lateinit var bar: ProgressBar

    private lateinit var dialog: Dialog<Void>

    override fun onCreated(root: BorderPane) {
        this.mini.graphic = SvgImageTranscoder.svgToImageView(MINI_ICON)
        this.dialog = Dialog()
        this.dialog.dialogPane = DialogPane()
        this.dialog.dialogPane.content = parent
        this.dialog.dialogPane.stylesheets.add(APP_STYLE)
        this.dialog.initStyle(StageStyle.UNDECORATED)
        this.dialog.showAndWait()
    }
}
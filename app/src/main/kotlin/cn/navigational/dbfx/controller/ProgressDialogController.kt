package cn.navigational.dbfx.controller

import cn.navigational.dbfx.Controller
import cn.navigational.dbfx.config.APP_STYLE
import cn.navigational.dbfx.config.MINI_ICON
import cn.navigational.dbfx.config.PROGRESS_DIALOG_PANE
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.stage.StageStyle

class ProgressDialogController : Controller<Void, BorderPane>(PROGRESS_DIALOG_PANE) {
    @FXML
    private lateinit var tip: Label

    @FXML
    private lateinit var des: Label

    @FXML
    private lateinit var mini: Button

    @FXML
    private lateinit var bar: ProgressBar

    private lateinit var dialog: Dialog<Void>

    companion object {
        const val DEFAULT_DIALOG_PANE_CLASS = "progress-dialog-pane"
    }

    override fun onCreated(root: BorderPane) {
        this.mini.graphic = SvgImageTranscoder.svgToImageView(MINI_ICON)
        this.dialog = Dialog()
        this.dialog.dialogPane = DialogPane()
        this.dialog.dialogPane.content = parent
        this.dialog.dialogPane.stylesheets.add(APP_STYLE)
        this.dialog.dialogPane.styleClass.add(DEFAULT_DIALOG_PANE_CLASS)
        this.dialog.initStyle(StageStyle.UNDECORATED)
        this.dialog.show()
    }

    fun closeDialog() {
        //Fix dialog can't close issue
        this.dialog.dialogPane.buttonTypes.add(ButtonType.OK)
        this.dialog.close()
    }

    /**
     * Update {@link TextType} update text info
     *
     * @param type Update type
     * @param text Update text info
     */
    fun updateText(type: TextType, text: String) {
        Platform.runLater {
            if (type == TextType.TIP) {
                this.tip.text = text
            } else {
                this.des.text = text
            }
        }
    }

    /**
     *
     * Text type
     *
     */
    enum class TextType {
        TIP,
        DES
    }
}
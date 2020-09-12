package cn.navigational.dbfx.dialog

import cn.navigational.dbfx.config.PROGRESS_DIALOG_PANE
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*

class ProgressDialog : UnDecoratedDialog<ButtonType>("Background Tasks", true, PROGRESS_DIALOG_PANE) {
    @FXML
    private lateinit var tip: Label

    @FXML
    private lateinit var des: Label

    @FXML
    private lateinit var bar: ProgressBar

    override fun closeDialog() {
        //Fix dialog can't close issue
        Platform.runLater {
            this.result = ButtonType.OK
            this.close()
        }
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
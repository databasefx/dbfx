package cn.navigational.dbfx.view

import cn.navigational.dbfx.ViewController
import cn.navigational.dbfx.config.EXPORT_PAGE
import cn.navigational.dbfx.config.FOLDER_X16
import cn.navigational.dbfx.i18n.I18N
import cn.navigational.dbfx.kit.utils.OssUtils
import cn.navigational.dbfx.tool.export.DataExportFactory
import cn.navigational.dbfx.tool.export.DataExportFactory.getExFormat
import cn.navigational.dbfx.tool.export.DataExportFactory.getFormat
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import javafx.beans.value.ChangeListener
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
import javafx.stage.Modality
import java.io.File

class ExportViewController(private val uuid: String, private val extData: String, private val target: ExportTarget) : ViewController<BorderPane>(EXPORT_PAGE) {

    @FXML
    private lateinit var extras: ChoiceBox<String>

    @FXML
    private lateinit var openFileSelector: Button

    @FXML
    private lateinit var listView: ListView<String>

    @FXML
    private lateinit var path: TextField

    private val selectChangeListener: ChangeListener<String> = ChangeListener { _, _, _ ->
        this.initPath()
    }

    init {
        this.initList()
        this.setSizeWithScreen(0.4, 0.4)
        this.stage.title = I18N.getString("stage.export.data")
        for (format in DataExportFactory.ExportFormat.values()) {
            this.extras.items.add(format.name)
        }
        this.openFileSelector.setOnAction { this.openDirSelector() }
        this.extras.selectionModel.select(0)
        this.initPath()
        this.openFileSelector.graphic = SvgImageTranscoder.svgToImageView(FOLDER_X16)
        this.extras.selectionModel.selectedItemProperty().addListener(this.selectChangeListener)
    }

    private fun initList() {
        if (target == ExportTarget.TABLE) {
            this.listView.items.add(extData)
            return
        }
        //Query table list
    }

    private fun initPath() {
        var path = if (target == ExportTarget.TABLE) {
            extData.substring(extData.lastIndexOf('.') + 1)
        } else {
            return
        }
        val format = getFormat(this.extras.selectionModel.selectedItem)
        path = "${OssUtils.getUserHome()}${File.separator}$path.${format}"
        this.path.text = path
    }

    private fun openDirSelector() {
        val format = getFormat(this.extras.selectionModel.selectedItem)
        val fc = FileChooser().also {
            it.initialDirectory = File(OssUtils.getUserHome())
            it.extensionFilters.add(FileChooser.ExtensionFilter("*.$format", format))
        }
        val fs = fc.showSaveDialog(this.stage) ?: return
        this.path.text = fs.absolutePath
    }

    @FXML
    fun cancel() {
        this.stage.close()
    }

    /**
     * Export target
     */
    enum class ExportTarget {
        /**
         * Single table
         */
        TABLE,

        /**
         * Scheme
         */
        SCHEME,
    }

    override fun dispose() {
        this.extras.selectionModel.selectedItemProperty().removeListener(this.selectChangeListener)
    }
}
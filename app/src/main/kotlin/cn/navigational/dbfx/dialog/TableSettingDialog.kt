package cn.navigational.dbfx.dialog

import cn.navigational.dbfx.config.TABLE_SETTING_PAGE
import cn.navigational.dbfx.model.TableSetting
import javafx.fxml.FXML
import javafx.scene.control.ButtonType
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField

class TableSettingDialog(private val setting: TableSetting) : UnDecoratedDialog<TableSetting>("表设置", TABLE_SETTING_PAGE) {
    @FXML
    private lateinit var global: CheckBox

    @FXML
    private lateinit var nulValue: TextField

    @FXML
    private lateinit var dfFormatChoice: ChoiceBox<String>

    init {
        this.result = setting
        nulValue.text = setting.nulFormat
        global.isSelected = setting.isGlobal
        dfFormatChoice.selectionModel.select(setting.dtFormat)
        dfFormatChoice.items.addAll("yyyy-MM-dd HH:mm:ss", "yyyyMMdd HH:mm:ss")
        this.dialogPane.buttonTypes.add(ButtonType.OK)
    }

    override fun closeDialog() {
        this.close()
    }
}
package cn.navigational.dbfx.dialog

import cn.navigational.dbfx.config.TABLE_SETTING_PAGE
import cn.navigational.dbfx.model.TableSetting
import javafx.fxml.FXML
import javafx.scene.control.ButtonType
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField

/**
 *
 *
 *Table data setting dialog box.
 *  The dialog box will use the setting object imported from outside as the benchmark for various settings.
 *  If the object returned by the dialog box and the input object input the same object,
 *  the parameters in the dialog box do not change;
 *  otherwise, the setting parameters of the dialog box will change
 * @author yangkui
 * @since 1.0
 */
class TableSettingDialog(private val setting: TableSetting) : UnDecoratedDialog<TableSetting>("表设置", TABLE_SETTING_PAGE) {
    @FXML
    private lateinit var global: CheckBox

    @FXML
    private lateinit var nulValue: TextField

    @FXML
    private lateinit var dfFormatChoice: ChoiceBox<String>

    init {
        this.result = setting
        nulValue.text = setting.nulValue
        global.isSelected = setting.isGlobal
        dfFormatChoice.selectionModel.select(setting.dtFormat)
        dfFormatChoice.items.addAll("yyyy-MM-dd HH:mm:ss", "yyyyMMdd HH:mm:ss")
        this.dialogPane.buttonTypes.add(ButtonType.OK)
        this.setResultConverter {
            return@setResultConverter if (it == ButtonType.OK) {
                val temp = TableSetting()
                temp.dtFormat = dfFormatChoice.value
                temp.isGlobal = global.isSelected
                temp.nulValue = nulValue.text
                temp
            } else {
                this.setting
            }
        }
    }

    override fun closeDialog() {
        this.close()
    }
}
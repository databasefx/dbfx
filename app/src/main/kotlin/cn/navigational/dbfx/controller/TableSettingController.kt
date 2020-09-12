package cn.navigational.dbfx.controller

import cn.navigational.dbfx.Controller
import cn.navigational.dbfx.config.TABLE_SETTING_PAGE
import cn.navigational.dbfx.controls.table.DataTableCell.Companion.NULL_VALUE
import javafx.scene.control.Dialog
import javafx.scene.control.DialogPane
import javafx.scene.layout.BorderPane
import javafx.stage.StageStyle

class TableSettingController : Controller<TableSettingController.TableSetting, BorderPane>(TABLE_SETTING_PAGE) {

    private lateinit var dialog: Dialog<TableSettingController>

    override fun onCreated(root: BorderPane?) {
        this.dialog = Dialog()
        this.dialog.dialogPane = DialogPane()
        this.dialog.dialogPane.content = parent
        this.dialog.showAndWait()
        this.dialog.initStyle(StageStyle.UNDECORATED)
    }

    inner class TableSetting() {
        /**
         * Datetime format pattern
         */
        private var dateTimeFormat: String = "yyyy-MM-dd HH:mm:ss"

        /**
         * Default null show value
         */
        private var nullFormat: String = NULL_VALUE
    }
}
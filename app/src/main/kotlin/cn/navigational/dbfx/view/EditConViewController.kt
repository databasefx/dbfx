package cn.navigational.dbfx.view

import cn.navigational.dbfx.DatabaseMetaManager
import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.ViewController
import cn.navigational.dbfx.config.APP_STYLE
import cn.navigational.dbfx.config.EDIT_CON_PAGE
import cn.navigational.dbfx.controller.ConInfoPaneController
import cn.navigational.dbfx.controls.tree.CustomTreeView
import cn.navigational.dbfx.i18n.I18N
import cn.navigational.dbfx.model.DatabaseMeta
import cn.navigational.dbfx.model.DbInfo
import javafx.fxml.FXML
import javafx.scene.control.ScrollPane
import javafx.scene.layout.BorderPane
import javafx.stage.Modality

class EditConViewController(private val info: DbInfo) : ViewController<ScrollPane>(EDIT_CON_PAGE) {

    private val dbMeta: DatabaseMeta

    private val controller: ConInfoPaneController = ConInfoPaneController()

    init {
        controller.initEdit(info)
        this.setSizeWithScreen(0.5, 0.7)
        this.scene.stylesheets.add(APP_STYLE)
        this.stage.initModality(Modality.WINDOW_MODAL)
        this.dbMeta = DatabaseMetaManager.getDbMeta(info.client)
        (this.parent.content as BorderPane).center = controller.parent
        this.stage.title = I18N.getString("stage.edit.connection")
    }

    @FXML
    fun cancel() {
        this.stage.close()
    }

    @FXML
    fun saveEdit() {
        val info = controller.getDbInfo(dbMeta)
        info.uuid = this.info.uuid
        //Update local cached
        SQLClientManager.updateDbInfo(info)
        //Require use select whether restart connection.
        CustomTreeView.getNavigator().updateConnection(info)
        this.stage.close()
    }
}
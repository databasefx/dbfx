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
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane
import javafx.stage.Modality

class EditConViewController(private val uuid: String) : ViewController<BorderPane>(EDIT_CON_PAGE) {

    private val dbMeta: DatabaseMeta

    private val controller: ConInfoPaneController = ConInfoPaneController()

    init {
        val dbInfo = SQLClientManager.manager.getDbInfo(uuid)
        this.dbMeta = DatabaseMetaManager.manager.getDbMeta(dbInfo.client)
        this.stage.title = I18N.getString("stage.edit.connection")
        controller.initEdit(dbInfo)
        this.scene.stylesheets.add(APP_STYLE)
        this.stage.initModality(Modality.WINDOW_MODAL)
        (scene.root as BorderPane).center = controller.parent
        this.setSizeWithScreen(0.5, 0.7)
    }

    @FXML
    fun cancel() {
        this.stage.close()
    }

    @FXML
    fun saveEdit() {
        val info = controller.getDbInfo(dbMeta)
        info.uuid = uuid
        //Update local cached
        SQLClientManager.manager.updateDbInfo(info)
        //Require use select whether restart connection.
        CustomTreeView.customTreeView.updateConnection(info)
        this.stage.close()
    }
}
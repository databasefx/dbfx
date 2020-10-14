package cn.navigational.dbfx.view

import cn.navigational.dbfx.DatabaseMetaManager
import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.View
import cn.navigational.dbfx.config.APP_STYLE
import cn.navigational.dbfx.config.EDIT_CON_PAGE
import cn.navigational.dbfx.controller.ConInfoPaneController
import cn.navigational.dbfx.controls.tree.CustomTreeView
import cn.navigational.dbfx.i18n.I18N
import cn.navigational.dbfx.model.DatabaseMeta
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Modality

class EditConView(private val uuid: String) : View<String>(EDIT_CON_PAGE, uuid) {

    private lateinit var dbMeta: DatabaseMeta

    private lateinit var controller: ConInfoPaneController

    override fun onCreated(scene: Scene, uuid: String) {
        this.title = I18N.getString("stage.edit.connection")
        //Init part filed
        this.controller = ConInfoPaneController()
        val dbInfo = SQLClientManager.manager.getDbInfo(uuid)
        this.dbMeta = DatabaseMetaManager.manager.getDbMeta(dbInfo.client)
        controller.initEdit(dbInfo)
        this.scene.stylesheets.add(APP_STYLE)
        this.initModality(Modality.WINDOW_MODAL)
        (scene.root as BorderPane).center = controller.parent
        this.show()
    }

    @FXML
    fun cancel() {
        this.close()
    }

    @FXML
    fun saveEdit() {
        val info = controller.getDbInfo(dbMeta)
        info.uuid = uuid
        //Update local cached
        SQLClientManager.manager.updateDbInfo(info)
        //Require use select whether restart connection.
        CustomTreeView.customTreeView.updateConnection(info)
        this.close()
    }
}
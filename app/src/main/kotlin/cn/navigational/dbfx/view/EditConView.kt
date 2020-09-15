package cn.navigational.dbfx.view

import cn.navigational.dbfx.View
import cn.navigational.dbfx.config.APP_STYLE
import cn.navigational.dbfx.controller.ConInfoPaneController
import cn.navigational.dbfx.model.DbInfo
import javafx.scene.Scene
import javafx.stage.Modality

class EditConView(private val dbInfo: DbInfo) : View<DbInfo>() {
    private val controller = ConInfoPaneController()

    init {
        controller.initEdit(dbInfo)
        this.title = "编辑连接"
        this.width = 700.0
        this.height = 600.0
        this.scene = Scene(controller.parent)
        this.scene.stylesheets.add(APP_STYLE)
        this.initModality(Modality.WINDOW_MODAL)
        this.isAlwaysOnTop = true
        this.show()
    }
}
package cn.navigational.dbfx.view


import cn.navigational.dbfx.ViewController
import cn.navigational.dbfx.config.SPLASH_PAGE

import javafx.application.Platform
import javafx.scene.layout.BorderPane
import javafx.stage.StageStyle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashViewController : ViewController<BorderPane>(SPLASH_PAGE) {
    init {
        this.stage.initStyle(StageStyle.UNDECORATED)
        this.showStage()
        GlobalScope.launch {
            cn.navigational.dbfx.io.init()
            Platform.runLater {
                stage.close()
                HomeViewController.home.showStage()
            }
        }
    }
}
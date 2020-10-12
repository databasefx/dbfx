package cn.navigational.dbfx.view


import cn.navigational.dbfx.View
import cn.navigational.dbfx.config.SPLASH_PAGE

import javafx.application.Platform
import javafx.scene.Scene
import javafx.stage.StageStyle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashView : View<Void>(SPLASH_PAGE) {
    override fun onCreated(root: Scene?) {
        initStyle(StageStyle.UNDECORATED)
        val that = this
        GlobalScope.launch {
            cn.navigational.dbfx.io.init()
            Platform.runLater {
                that.close()
                HomeView()
            }
        }
    }

    companion object {
        fun requireStart() {
            SplashView()
        }
    }
}
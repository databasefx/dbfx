package cn.navigational.dbfx.controller

import cn.navigational.dbfx.config.B_N_TERMINAL_PANE
import cn.navigational.dbfx.controller.BottomNavigationExpandPaneController.*
import javafx.scene.control.MenuItem
import javafx.scene.layout.BorderPane

class TerminalController : ExpandPaneProvider<Void, BorderPane>(B_N_TERMINAL_PANE) {

    override fun getSetting(): MutableList<MenuItem> {
        return arrayListOf()
    }

    override fun getTitle(): String {
        return "终端"
    }

    override fun close() {
    }
}
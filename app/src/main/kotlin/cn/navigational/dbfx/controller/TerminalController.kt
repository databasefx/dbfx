package cn.navigational.dbfx.controller

import cn.navigational.dbfx.config.B_N_TERMINAL_PANE
import cn.navigational.dbfx.controller.BottomNavigationExpandPaneAbstractFxmlController.*
import cn.navigational.dbfx.i18n.I18N
import javafx.scene.control.MenuItem
import javafx.scene.layout.BorderPane

class TerminalController : ExpandPaneProvider<BorderPane>(B_N_TERMINAL_PANE) {

    override fun getSetting(): MutableList<MenuItem> {
        return arrayListOf()
    }

    override fun getTitle(): String {
        return I18N.getString("tool.bar.terminal")
    }

    override fun close() {
    }
}
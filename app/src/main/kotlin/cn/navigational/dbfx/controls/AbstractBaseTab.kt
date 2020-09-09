package cn.navigational.dbfx.controls

import javafx.scene.control.Tab

abstract class AbstractBaseTab : Tab() {
    /**
     * When tab instance after init data call that method
     */
    abstract suspend fun init()

    /**
     * When current tab request close call that method
     */
    abstract suspend fun close()
}
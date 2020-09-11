package cn.navigational.dbfx.navigator

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.handler.NavigatorMenuHandler
import cn.navigational.dbfx.model.DbInfo
import cn.navigational.dbfx.model.SQLClient
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import cn.navigational.dbfx.handler.NavigatorMenuHandler.Companion.MenuType.*
import javafx.application.Platform
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty

abstract class DatabaseItem(private val dbInfo: DbInfo, icon: String) : BaseTreeItem<String>(icon) {
    /**
     *
     * Current database is connection?
     *
     */
    protected val connectStatus: BooleanProperty = SimpleBooleanProperty(false, "conStatus")

    /**
     *
     * SQL Client property
     *
     */
    private val client: ObjectProperty<SQLClient> = SimpleObjectProperty(null, "sqlClient")

    init {

        this.value = dbInfo.name
        val handler = NavigatorMenuHandler.init(supportMenu)

        val sCon = handler.getMenuItem("连接", OPEN_CONNECT, this::startConnect)
        val eCon = handler.getMenuItem("断开", DIS_CONNECT, this::endConnect, true)
        val ter = handler.getMenuItem("SQL终端", SQL_TERMINAL, this::openTerminal, true)
        val flush = handler.getMenuItem("刷新", FLUSH, this::flush, true)
        handler.getMenuItem("编辑连接", EDIT_CONNECT, this::edit)

        connectStatus.addListener { _, _, n ->
            Platform.runLater {
                sCon.isDisable = n
                eCon.isDisable = !n
                ter.isDisable = !n
                flush.isDisable = !n
            }
        }
    }

    protected suspend fun initClient() {
        endConnect()
        client.value = SQLClientManager.manager.createClient(dbInfo)
    }

    fun getSQLClient(): SQLClient {
        if (client.value != null) {
            return client.value
        }
        throw RuntimeException("Current sql client is null!")
    }

    /**
     * Start to connection database
     */
    abstract suspend fun startConnect()

    /**
     *
     * End a connect
     *
     */
    private suspend fun endConnect() {
        val that = this
        if (client.value !== null) {
            SQLClientManager.manager.removeClient(client.value.uuid)
            client.value = null
        }
        that.children.clear()
        connectStatus.value = false
    }

    /**
     *
     * Open SQL terminal
     *
     */
    abstract suspend fun openTerminal()

    /**
     *
     * Flush database
     *
     */
    abstract suspend fun flush()

    /**
     * Edit current database
     */
    abstract suspend fun edit()
}
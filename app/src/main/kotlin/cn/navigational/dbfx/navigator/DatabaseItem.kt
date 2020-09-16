package cn.navigational.dbfx.navigator

import cn.navigational.dbfx.BaseTreeItem
import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.handler.NavigatorMenuHandler
import cn.navigational.dbfx.model.DbInfo
import cn.navigational.dbfx.model.SQLClient
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import cn.navigational.dbfx.handler.NavigatorMenuHandler.Companion.MenuType.*
import cn.navigational.dbfx.view.EditConView
import javafx.application.Platform
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty

abstract class DatabaseItem(val uuid: String, icon: String) : BaseTreeItem<String>(icon) {
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
        this.update()
        val handler = NavigatorMenuHandler.init(supportMenu)
        val sCon = handler.getMenuCoroutine("连接", OPEN_CONNECT, this::startConnect)
        val eCon = handler.getMenuCoroutine("断开", DIS_CONNECT, this::endConnect, true)
        val flush = handler.getMenuCoroutine("刷新", FLUSH, this::flush, true)
        val ter = handler.getMenuUnCoroutine("SQL终端", SQL_TERMINAL, this::openTerminal, true)
        handler.getMenuUnCoroutine("编辑连接", EDIT_CONNECT, this::edit)
        //Remove current database
        handler.getMenuUnCoroutine("移除连接", REMOVE_DB, { SQLClientManager.manager.removeDbInfo(uuid) })

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
        client.value = SQLClientManager.manager.createClient(uuid)
    }

    fun getSQLClient(): SQLClient {
        if (client.value != null) {
            return client.value
        }
        logger.error("Current uuid:{} not connection!", uuid)
        throw RuntimeException("Current uuid:[${uuid}] not connection!")
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
        if (client.value !== null) {
            SQLClientManager.manager.removeClient(client.value.uuid)
            client.value = null
        }
        this.children.clear()
        connectStatus.value = false
    }

    /**
     *
     * Open SQL terminal
     *
     */
    private fun openTerminal() {

    }

    /**
     *
     * Flush database
     *
     */
    abstract suspend fun flush()

    /**
     * Edit current database
     */
    private fun edit() {
        EditConView(uuid)
    }

    /**
     * Delete current database node
     */
    fun delete() {
        //If current database is open status,fire close event.
        for (menu in supportMenu) {
            if (menu.text == "断开" && !menu.isDisable) {
                menu.fire()
                break
            }
        }
        //Remove current TreeItem form parent node
        parent.children.remove(this)
    }

    fun getConnectionStatus(): Boolean {
        return this.connectStatus.get()
    }

    /**
     * Update current database base info,for example value,icon...etc.
     */
    fun update() {
        val info = getDbInfo()
        this.value = info.name
    }

    private fun getDbInfo(): DbInfo {
        return SQLClientManager.manager.getDbInfo(uuid)
    }

}
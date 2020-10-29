package cn.navigational.dbfx.controls.tree

import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.controls.tab.SQLTerminalTab
import cn.navigational.dbfx.controls.tree.folder.RoleFolder
import cn.navigational.dbfx.controls.tree.folder.SchemeFolder
import cn.navigational.dbfx.controls.tree.impl.ProgressTreeItem
import cn.navigational.dbfx.controls.tree.scheme.SchemeItem
import cn.navigational.dbfx.handler.MainTabPaneHandler
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.utils.StringUtils
import cn.navigational.dbfx.model.DbInfo
import cn.navigational.dbfx.model.SQLClient
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder
import cn.navigational.dbfx.utils.AlertUtils
import cn.navigational.dbfx.view.CreateConViewController
import cn.navigational.dbfx.view.EditConViewController
import javafx.scene.input.MouseEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean


class DatabaseTreeItem constructor(private var info: DbInfo) : ProgressTreeItem(SvgImageTranscoder.svgToImageView(SQLClient.getMiniIcon(info.client))) {
    /**
     * Current database connection status
     */
    private val conStatus: AtomicBoolean = AtomicBoolean(false)

    init {
        this.update(info)
        this.contextMenu.updateItem(
                ContextMenuAction.ADD,
                TreeItemMenuHandler.MenuAction.OPEN_CONNECT,
                TreeItemMenuHandler.MenuAction.CREATE_COPY,
                TreeItemMenuHandler.MenuAction.FLUSH,
                TreeItemMenuHandler.MenuAction.OPEN_TERMINAL,
                TreeItemMenuHandler.MenuAction.DISCOUNT_CONNECT,
                TreeItemMenuHandler.MenuAction.EDIT_CONNECT,
                TreeItemMenuHandler.MenuAction.DELETE_CONNECT)
    }

    fun update(info: DbInfo) {
        this.info = info
        this.text = info.name
        this.setSuffixTx(info.comment)
        if (StringUtils.isNotEmpty(info.comment)) {
            this.suffix.styleClass.add(AbstractBaseTreeItem.DATA_INDICATOR_STYLE_CLASS)
        } else {
            this.suffix.styleClass.remove(AbstractBaseTreeItem.DATA_INDICATOR_STYLE_CLASS)
        }
    }

    private fun startConnect() {
        if (loadStatus.get() || conStatus.get()) {
            return
        }
        GlobalScope.launch {
            loadStatus.set(true)
            try {
                val client = createClient()
                initConnection(client)
                conStatus.set(true)
            } catch (e: Exception) {
                SQLClientManager.removeClient(info.uuid)
                AlertUtils.showExDialog("Connection failed!", e)
            } finally {
                loadStatus.set(false)
            }
        }
    }

    private suspend fun createClient(): SQLClient {
        val query = SQLQuery.getClQuery(info.client)
        val client = SQLClientManager.createClient(info.uuid)
        client.version = query.showDbVersion(client.client)
        SQLClientManager.addClient(client)
        return client
    }

    private suspend fun initConnection(sqlClient: SQLClient) {
        val cl = sqlClient.cl
        when (cl!!) {
            Clients.MYSQL -> initMysql()
            Clients.DB2 -> initDb2()
            Clients.POSTGRESQL -> initPg(sqlClient)
            Clients.MS_SQL -> initSqlServer()
        }
    }

    private suspend fun discount() {
        if (loadStatus.get() || !conStatus.get()) {
            return
        }
        loadStatus.set(true)
        try {
            MainTabPaneHandler.closeTabByPathPrefix(fullPath)
            clear()
            SQLClientManager.removeClient(info.uuid)
            conStatus.set(false)
        } finally {
            loadStatus.set(false)
        }
    }

    private suspend fun initPg(client: SQLClient) {
        val query = SQLQuery.getClQuery(Clients.POSTGRESQL)
        val list = query.showDatabase(client.client)
        val index = list.indexOf(client.dbInfo.database)
        if (index == -1) {
            return
        }
        val database = list[index]
        val schemeItem = SchemeItem(database, client.uuid)
        addChildren(schemeItem)
    }

    private fun initMysql() {
        addChildren(SchemeFolder(getUuId()), RoleFolder(getUuId(), null))
    }

    fun delConnect() {
        GlobalScope.launch {
            discount()
            removeItem(treeItem)
            //Delete from disk
            SQLClientManager.removeDbInfo(info.uuid)
        }
    }

    suspend fun openTerminal() {
        MainTabPaneHandler.addTabToPane(SQLTerminalTab(getUuId()), fullPath)
    }

    fun reConnect() {
        GlobalScope.launch {
            discount()
            startConnect()
        }
    }

    private suspend fun initDb2() {

    }

    private suspend fun initSqlServer() {

    }

    override fun onAction(action: TreeItemMenuHandler.MenuAction) {
        when (action) {
            TreeItemMenuHandler.MenuAction.FLUSH -> this.reConnect()
            TreeItemMenuHandler.MenuAction.DELETE_CONNECT -> this.delConnect()
            TreeItemMenuHandler.MenuAction.OPEN_CONNECT -> this.startConnect()
            TreeItemMenuHandler.MenuAction.DISCOUNT_CONNECT -> GlobalScope.launch { discount() }
            TreeItemMenuHandler.MenuAction.EDIT_CONNECT -> EditConViewController(info).showStage()
            TreeItemMenuHandler.MenuAction.OPEN_TERMINAL -> GlobalScope.launch { openTerminal() }
            TreeItemMenuHandler.MenuAction.CREATE_COPY -> CreateConViewController(info).showStage()

        }
    }

    override fun onMouseClicked(event: MouseEvent) {
        super.onMouseClicked(event)
        val count = event.clickCount
        if (count > 1) {
            this.startConnect()
        }
    }

    fun conStatus(): Boolean {
        return this.conStatus.get()
    }


    fun getUuId(): String {
        return this.info.uuid
    }
}
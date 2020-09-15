package cn.navigational.dbfx

import cn.navigational.dbfx.io.updateDbInfoList
import cn.navigational.dbfx.model.DbInfo
import cn.navigational.dbfx.model.SQLClient
import cn.navigational.dbfx.kit.SqlClientFactory
import cn.navigational.dbfx.kit.utils.VertxUtils
import io.vertx.kotlin.coroutines.await
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlClient
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.util.concurrent.ConcurrentHashMap

/**
 *
 *
 * SQL client session manager
 *
 * @author yangkui
 * @since 1.0
 */
class SQLClientManager {
    /**
     *
     * Cached all database info use ArrayList
     *
     */
    private val dbList: ObservableList<DbInfo> = FXCollections.observableArrayList()

    /**
     * Cached all running sql client
     */
    private val clients: MutableMap<String, SQLClient> = ConcurrentHashMap()

    fun createClient(info: DbInfo): SQLClient {

        val cl = info.client
        val options = SqlClientFactory.createConnectionOptions(cl)

        options.host = info.host
        options.port = info.port
        options.user = info.username
        options.password = info.password
        options.database = info.database

        val client = SQLClient()
        client.cl = cl
        client.dbInfo = info
        client.uuid = info.uuid
        client.client = SqlClientFactory.createClient(VertxUtils.getVertx(), options, defaultPoolOptions, cl)
        return client
    }

    /**
     * Add client into  clients
     * @param client Target client
     */
    fun addClient(client: SQLClient) {
        val uuid = client.uuid
        if (clients.containsKey(uuid)) {
            throw RuntimeException("Repeat add client [uuid:$uuid]")
        }
        clients[uuid] = client
        println("Success add ${client.uuid} into clients")
    }

    /**
     * Remove from clients according by client uuid
     *
     * @param uuid Client uuid
     */
    suspend fun removeClient(uuid: String) {
        if (!clients.containsKey(uuid)) {
            println("Target $uuid not found!")
            return
        }
        val client = clients[uuid]!!
        client.client.close().await()
        clients.remove(uuid)
        println("Success remove $uuid client.")
    }

    /**
     *
     * Close all client
     *
     */
    suspend fun closeAllClient() {
        clients.keys.forEach { key ->
            val client = clients[key]!!
            client.client.close().await()
            println("Success close $key client.")
        }
        clients.clear()
    }

    fun getDbInfo(): ObservableList<DbInfo> {
        return dbList
    }

    /**
     *
     * From cached get a sql client instance if exist,or else get null
     *@param uuid Sql client uuid
     */
    fun getClient(uuid: String): SQLClient? {
        var client: SQLClient? = null
        if (this.clients.containsKey(uuid)) {
            client = this.clients[uuid]
        }
        return client
    }

    /**
     *Remove database info from list
     *
     * @param info Target database info
     */
    fun removeDbInfo(info: DbInfo) {
        if (!this.dbList.contains(info)) {
            return
        }
        this.dbList.remove(info)
        //Update cached database info
        updateDbInfoList(this.dbList)
    }

    /**
     *
     * Update/Add database info
     * @param dbInfo database info
     */
    fun updateDbInfo(dbInfo: DbInfo) {
        var index = -1
        val uuid = dbInfo.uuid
        this.dbList.forEachIndexed { _index, info ->
            if (info.uuid == uuid) {
                index = _index
            }
        }
        //If current list not exist target inf,add a new database info
        if (index == -1) {
            this.dbList.add(dbInfo)
        } else {
            this.dbList[index].updateField(dbInfo)
        }
        updateDbInfoList(this.dbList)
    }

    suspend fun closeSQLClient(client: SqlClient) {
        client.close().await()
    }


    companion object {
        /**
         * Default Sql client pool options
         */
        private val defaultPoolOptions = PoolOptions().setMaxSize(10).setMaxWaitQueueSize(10)
        val manager: SQLClientManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { SQLClientManager() }
    }
}
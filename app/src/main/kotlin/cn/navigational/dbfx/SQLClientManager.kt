package cn.navigational.dbfx

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

    private fun createClient(info: DbInfo): SQLClient {

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
        val clients = clientManager.clients
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
            throw RuntimeException("Target $uuid not found!")
        }
        val client = clients[uuid]!!
        client.client.close().await()
        clients.remove(uuid)
        println("Success remove $uuid client.")
    }


    companion object {
        private val clientManager: SQLClientManager = SQLClientManager()

        /**
         * Default Sql client pool options
         */
        private val defaultPoolOptions = PoolOptions().setMaxSize(10).setMaxWaitQueueSize(10)

        suspend fun closeSQLClient(client: SqlClient) {
            client.close().await()
        }

        fun addDbInfo(dbInfo: DbInfo) {
            clientManager.dbList.add(dbInfo)
        }

        fun removeDbInfo(dbInfo: DbInfo) {
            clientManager.dbList.remove(dbInfo)
        }

        fun getDbInfo(): ObservableList<DbInfo> {
            return clientManager.dbList
        }

        fun createClient(info: DbInfo): SQLClient {
            return clientManager.createClient(info)
        }

        fun addClient(client: SQLClient) {
            clientManager.addClient(client)
        }

        suspend fun removeClient(uuid: String) {
            clientManager.removeClient(uuid)
        }
    }
}
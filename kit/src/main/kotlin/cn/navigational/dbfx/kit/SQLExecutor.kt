package cn.navigational.dbfx.kit

import cn.navigational.dbfx.kit.enums.Clients
import io.vertx.kotlin.coroutines.await
import io.vertx.sqlclient.*

/**
 *
 *
 * Abstract sql executor interface
 *
 * @author yangkui
 * @since 1.0
 *
 */
interface SQLExecutor {
    companion object {
        /**
         *
         * Execute single sql without params
         *
         * @param cl target client
         * @param client target execute source
         */
        suspend fun executeSql(sql: String, cl: Clients, client: SqlClient): RowSet<Row> {
            return client.query(sql).execute().await()
        }

        /**
         *
         * Execute single sql with param
         * @param cl target client
         * @param client target execute source
         * @param tuple sql params
         *
         */
        suspend fun executeSql(sql: String, cl: Clients, client: SqlClient, tuple: Tuple): RowSet<Row> {
            return client.preparedQuery(sql).execute(tuple).await()
        }

        /**
         *
         * Batch execute sql statement
         *
         * @param cl target client
         * @param client target execute source
         * @param tuples batch execute param
         *
         */
        suspend fun executeSql(sql: String, cl: Clients, client: SqlClient, tuples: List<Tuple>): RowSet<Row> {
            return client.preparedQuery(sql).executeBatch(tuples).await()
        }

        /**
         *
         * From client get a connection
         *
         * @param cl target client
         * @param client target client
         *
         */
        suspend fun getConnection(cl: Clients, client: Pool): SqlConnection {
            return client.connection.await()
        }
    }
}
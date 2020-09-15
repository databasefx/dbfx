package cn.navigational.dbfx.kit

import cn.navigational.dbfx.kit.enums.Clients
import io.vertx.kotlin.coroutines.await
import io.vertx.sqlclient.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *
 *
 * Abstract sql executor interface
 *
 * @author yangkui
 * @since 1.0
 *
 */
class SQLExecutor {
    companion object {

        private val logger: Logger = LoggerFactory.getLogger(SQLQuery::class.java)

        /**
         *
         * Execute single sql without params
         *
         * @param cl target client
         * @param client target execute source
         */
        suspend fun executeSql(sql: String, cl: Clients, client: SqlClient): RowSet<Row> {
            log(cl, sql)
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
            log(cl, sql, arrayListOf(tuple))
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
            log(cl, sql, tuples)
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

        /**
         *
         * Output sql execute log
         * @param cl Current execute sql Client
         * @param sql Target execute sql statement
         * @param tuples Target execute sql tuples
         */
        private fun log(cl: Clients, sql: String, tuples: List<Tuple> = arrayListOf()) {
            if (!logger.isDebugEnabled) {
                return
            }
            val tus = StringBuffer("[")
            for (tuple in tuples) {
                val size = tuple.size()
                var s = "["
                for (i in 0 until size) {
                    val value = tuple.getValue(i)
                    s += value?.toString() ?: "null"
                    if (i != size - 1) {
                        s += ","
                    }
                }
                s += "],"
                tus.append(s)
            }
            //remove last ','
            if (tuples.isNotEmpty()) {
                val index = tus.lastIndexOf(",")
                tus.replace(index, index + 1, "")
            }
            tus.append("]")
            val log = """
                ############################# SQL Execute Detail ######################################
                 Execute-Client:
                    ${cl.client}                                       
                 Execute-Sql:
                    $sql
                 Execute-Tuples:
                    $tus
                #######################################################################################
            """.trimIndent()
            logger.debug("\r\n{}", log)
        }
    }
}
package cn.navigational.dbfx.kit

import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.model.TableColumnMeta
import cn.navigational.dbfx.kit.mysql.MysqlQuery
import cn.navigational.dbfx.kit.postgres.PgQuery
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.SqlClient

interface SQLQuery {
    companion object {
        fun getClQuery(cl: Clients): SQLQuery {
            return when (cl) {
                Clients.MYSQL -> MysqlQuery()
                else -> PgQuery()
            }
        }
    }

    /**
     * Show current client for database info
     */
    suspend fun showDbInfo(client: SqlClient): RowSet<Row>

    /**
     *
     * Show current client version
     *
     */
    suspend fun showDbVersion(client: SqlClient): String

    /**
     * Query current client all database
     */
    suspend fun showDatabase(client: SqlClient): List<String>

    /**
     * Query current client all collation
     */
    suspend fun showCollations(client: SqlClient): List<String>

    /**
     * Query current client user list
     */
    suspend fun queryDbUser(client: SqlClient): List<String>

    /**
     * Query table list from fix category
     */
    suspend fun showTable(category: String, client: SqlClient): List<String>

    /**
     * Query a table all field
     */
    suspend fun showTableField(category: String, table: String, client: SqlClient): List<TableColumnMeta>

    /**
     * Paging query table data
     */
    suspend fun pageQuery(category: String, table: String, pageIndex: Int, pageSize: Int, client: SqlClient): RowSet<Row>

    /**
     * Get target table data total
     */
    suspend fun queryTableTotal(category: String, table: String, client: SqlClient): Long
}
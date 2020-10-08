package cn.navigational.dbfx.kit.postgres

import cn.navigational.dbfx.kit.SQLExecutor
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.enums.DataType
import cn.navigational.dbfx.kit.model.TableColumnMeta
import io.vertx.kotlin.coroutines.await
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple

class PgQuery : SQLQuery {
    /**
     * More detail please visit <a href='https://www.postgresql.org/docs/9.1/sql-show.html'></a>
     */
    override suspend fun showDbInfo(client: SqlClient): RowSet<Row> {
        val sql = "SHOW ALL"
        return SQLExecutor.executeSql(sql, Clients.POSTGRESQL, client)
    }

    override suspend fun showDbVersion(client: SqlClient): String {
        val sql = "SHOW server_version"
        val res = SQLExecutor.executeSql(sql, Clients.POSTGRESQL, client)
        var version = ""
        for (row in res) {
            version = row.getString("server_version")
            break
        }
        return version
    }

    override suspend fun showDatabase(client: SqlClient): List<String> {
        val sql = "SELECT datname FROM pg_database"
        val rows = SQLExecutor.executeSql(sql, Clients.POSTGRESQL, client)
        val list = arrayListOf<String>()
        for (row in rows) {
            list.add(row.getString("datname"))
        }
        return list
    }

    override suspend fun showCollations(client: SqlClient): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun queryDbUser(client: SqlClient): List<String> {
        val sql = "SELECT * FROM pg_roles"
        val rows = SQLExecutor.executeSql(sql, Clients.POSTGRESQL, client)
        val list = arrayListOf<String>()
        for (row in rows) {
            val roleName = row.getString("rolname")
            list.add(roleName)
        }
        return list
    }

    override suspend fun showTable(category: String, client: SqlClient): List<String> {
        return showTable("BASE TABLE", category, client)
    }

    override suspend fun showTableField(category: String, table: String, client: SqlClient): List<TableColumnMeta> {
        val dbName = category.split(".")[0]
        val scheme = category.split(".")[1]
        val sql = "SELECT * FROM ${dbName}.information_schema.columns WHERE table_schema=$1 AND table_name=$2"
        val tuple = Tuple.of(scheme, table)
        val rowSet = SQLExecutor.executeSql(sql, Clients.POSTGRESQL, client, tuple)
        val list = arrayListOf<TableColumnMeta>()
        for (row in rowSet) {
            val columnMeta = TableColumnMeta()
            columnMeta.colName = row.getString("column_name")
            columnMeta.dataType = DataType.STRING
            columnMeta.type = row.getString("data_type")
            columnMeta.length = 0
            columnMeta.position = row.getInteger("ordinal_position")
            columnMeta.isNullable = row.getString("is_nullable") == "YES"
            columnMeta.comment = ""
            list.add(columnMeta)
        }
        return list
    }

    override suspend fun pageQuery(category: String, table: String, pageIndex: Int, pageSize: Int, client: SqlClient): RowSet<Row> {
        val offset = (pageIndex - 1) * pageSize
        val sql = "SELECT * FROM ${category}.$table LIMIT $1 offset $2"
        val tuple = Tuple.of(pageSize, offset)
        return SQLExecutor.executeSql(sql, Clients.POSTGRESQL, client, tuple)
    }

    override suspend fun queryTableTotal(category: String, table: String, client: SqlClient): Long {
        val sql = "SELECT COUNT(*) FROM ${category}.$table"
        val rowSet = SQLExecutor.executeSql(sql, Clients.POSTGRESQL, client)
        var total = 0L
        for (row in rowSet) {
            total = row.getValue(0) as Long
        }
        return total
    }

    suspend fun queryDbScheme(dbName: String, client: SqlClient): List<String> {
        val sql = "SELECT schema_name FROM ${dbName}.information_schema.schemata"
        val rowSet = SQLExecutor.executeSql(sql, Clients.POSTGRESQL, client)
        val list = arrayListOf<String>()
        for (row in rowSet) {
            list.add(row.getValue(0).toString())
        }
        return list
    }

    override suspend fun showView(category: String, client: SqlClient): List<String> {
        return showTable("VIEW", category, client)
    }

    private suspend fun showTable(tableType: String, category: String, client: SqlClient): List<String> {
        val dbName = category.split(".")[0]
        val scheme = category.split(".")[1]
        val sql = "SELECT table_name FROM ${dbName}.information_schema.tables WHERE table_schema=$1 AND table_type=$2"
        val tuple = Tuple.of(scheme, tableType)
        val rowSet = SQLExecutor.executeSql(sql, Clients.POSTGRESQL, client, tuple)
        val list = arrayListOf<String>()
        for (row in rowSet) {
            list.add(row.getValue(0).toString())
        }
        list.sortBy { !it.startsWith("_") }
        return list
    }
}
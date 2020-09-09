package cn.navigational.dbfx.kit.postgres

import cn.navigational.dbfx.kit.SQLExecutor
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.model.TableColumnMeta
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.SqlClient

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
        TODO("Not yet implemented")
    }

    override suspend fun showTableField(category: String, table: String, client: SqlClient): List<TableColumnMeta> {
        TODO("Not yet implemented")
    }

    override suspend fun pageQuery(category: String, table: String, pageIndex: Int, pageSize: Int, client: SqlClient): List<List<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun queryTableTotal(category: String, table: String, client: SqlClient): Long {
        TODO("Not yet implemented")
    }
}
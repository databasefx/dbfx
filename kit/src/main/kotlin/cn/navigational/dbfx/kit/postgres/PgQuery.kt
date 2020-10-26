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
import java.util.HashMap

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
        val sql = "SELECT datname FROM pg_database WHERE datistemplate!='true'"
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
        val sql = "SELECT rolname FROM pg_roles"
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
        val prefix = "$dbName.pg_catalog"
        val sql = """SELECT col_description(a.attrelid,a.attnum) as comment,format_type(a.atttypid,a.atttypmod) as type,
                a.attname as name, a.attnotnull as notnull,a.attlen as len,a.attnum pos FROM $prefix.pg_class as c,$prefix.pg_attribute as a 
                WHERE c.relname ='$table' and a.attrelid = c.oid and a.attnum>0"""
        val list = arrayListOf<TableColumnMeta>()
        val rowSet = SQLExecutor.executeSql(sql, Clients.POSTGRESQL, client)
        for (row in rowSet) {
            val columnMeta = TableColumnMeta()
            columnMeta.constrainTypes = arrayOf()
            columnMeta.colName = row.getString("name")
            columnMeta.dataType = DataType.STRING
            columnMeta.type = row.getString("type")
            columnMeta.length = row.getInteger("len")
            columnMeta.position = row.getInteger("pos")
            columnMeta.isNullable = row.getBoolean("notnull")
            columnMeta.comment = row.getString("comment")
            columnMeta.extraAttr = if (columnMeta.type == "serial")
                arrayOf(TableColumnMeta.TableColumnExtraAttr.AUTO_INCREMENT) else arrayOf()
            list.add(columnMeta)
        }
        val constrains = getTableConstrain(table, dbName, client)
        for (meta in list) {
            val value = constrains[meta.colName]
            meta.constrainTypes = PgSQLHelper.getTableConstrain(value)
        }
        return list
    }

    private suspend fun getTableConstrain(table: String, category: String, client: SqlClient): Map<String, String> {
        val sql = "SELECT c.column_name AS name, tc.constraint_type AS type FROM $category.information_schema.table_constraints tc " +
                "JOIN $category.information_schema.constraint_column_usage AS ccu USING (constraint_schema, constraint_name) " +
                "JOIN $category.information_schema.columns AS c ON c.table_schema = tc.constraint_schema " +
                "AND tc.table_name = c.table_name AND ccu.column_name = c.column_name and tc.table_name = $1"
        val constrains: MutableMap<String, String> = HashMap()
        val rowSet = SQLExecutor.executeSql(sql, Clients.POSTGRESQL, client, Tuple.of(table))
        for (row in rowSet) {
            val type = row.getString("type")
            val name = row.getString("name")
            constrains[name] = type
        }
        return constrains
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
package cn.navigational.dbfx.kit.mysql

import cn.navigational.dbfx.kit.SQLExecutor
import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.config.NULL_TAG
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.model.TableColumnMeta
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.SqlClient
import io.vertx.sqlclient.Tuple

class MysqlQuery : SQLQuery {

    override suspend fun showDbInfo(client: SqlClient): RowSet<Row> {
        val sql = "SHOW VARIABLES"
        return SQLExecutor.executeSql(sql, Clients.MYSQL, client)
    }

    override suspend fun showDbVersion(client: SqlClient): String {
        val rowSet = showDbInfo(client)
        return MysqlHelper.getMysqlVersion(rowSet)
    }

    override suspend fun showDatabase(client: SqlClient): List<String> {
        val sql = "SHOW DATABASES"
        val rows = SQLExecutor.executeSql(sql, Clients.MYSQL, client)
        val list = arrayListOf<String>()
        for (row in rows) {
            val name = row.getString("Database")
            list.add(name)
        }
        return list
    }

    override suspend fun showCollations(client: SqlClient): List<String> {
        val sql = "SHOW COLLATION"
        val rowSet = SQLExecutor.executeSql(sql, Clients.MYSQL, client)
        val list = arrayListOf<String>()
        for (row in rowSet) {
            list.add(row.getString("Collation"))
        }
        return list
    }

    override suspend fun queryDbUser(client: SqlClient): List<String> {
        val sql = "SELECT user,host FROM mysql.user"
        val rowSet = SQLExecutor.executeSql(sql, Clients.MYSQL, client)
        val list = arrayListOf<String>()
        for (row in rowSet) {
            val user = row.getString("user")
            val host = row.getString("host")
            list.add("$user@$host")
        }
        return list
    }

    override suspend fun showTable(category: String, client: SqlClient): List<String> {
        val sql = "SELECT TABLE_NAME FROM `information_schema`.`TABLES` WHERE (`table_type` ='BASE TABLE' OR `TABLE_TYPE`='SYSTEM VIEW') AND table_schema =?"
        val rowSet = SQLExecutor.executeSql(sql, Clients.MYSQL, client, Tuple.of(category))
        val list = arrayListOf<String>()
        for (row in rowSet) {
            list.add(row.getString("TABLE_NAME"))
        }
        return list
    }

    override suspend fun showTableField(category: String, table: String, client: SqlClient): List<TableColumnMeta> {
//        val name = "`$category`.`$table`"
        val sql = "SELECT COLUMN_NAME,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH," +
                "NUMERIC_SCALE,COLUMN_KEY,COLUMN_COMMENT,ORDINAL_POSITION FROM information_schema.COLUMNS" +
                " WHERE TABLE_SCHEMA=? AND  TABLE_NAME=?"
//        val sql = "SHOW FULL COLUMNS FROM $name"
        val tuple = Tuple.of(category, table)
        val rowSet = SQLExecutor.executeSql(sql, Clients.MYSQL, client, tuple)
        val list = arrayListOf<TableColumnMeta>()
        for (row in rowSet) {
            val columnMeta = TableColumnMeta()
            val column = row.getString("COLUMN_NAME")
            val comment = row.getString("COLUMN_COMMENT")
            val length = row.getInteger("CHARACTER_MAXIMUM_LENGTH")
            val dataType = row.getString("DATA_TYPE")
            val pos = row.getInteger("ORDINAL_POSITION")
            columnMeta.colName = column
            columnMeta.comment = comment
            columnMeta.length = length
            columnMeta.type = dataType
            columnMeta.position = pos
            columnMeta.dataType = getMyDataType(dataType)
            list.add(columnMeta)
        }
        list.sortBy { it.position }
        return list
    }

    override suspend fun pageQuery(category: String, table: String, pageIndex: Int, pageSize: Int, client: SqlClient): RowSet<Row> {
        val name = "`$category`.`$table`"
        val sql = "SELECT * FROM $name LIMIT ?,?"
        val offset = MysqlPageHelper.getStartNum(pageIndex, pageSize)
        val tuple = Tuple.of(offset, pageSize)

        return SQLExecutor.executeSql(sql, Clients.MYSQL, client, tuple)

//        val list = arrayListOf<List<String>>()
//        rowSet.forEach {
//            val size = it.size()
//            val row = arrayListOf<String>()
//            for (i in 0 until it.size()) {
//                val value = it.getValue(i)
//                if (value !== null) {
//                    row.add(value.toString())
//                } else {
//                    row.add(NULL_TAG)
//                }
//            }
//            list.add(row)
//        }
//        return list
    }

    override suspend fun queryTableTotal(category: String, table: String, client: SqlClient): Long {
        val name = MysqlHelper.transTableName(category, table)
        val sql = "SELECT COUNT(*) FROM $name"
        val rowSet = SQLExecutor.executeSql(sql, Clients.MYSQL, client)
        var totalNumber = 0L
        for (row in rowSet) {
            totalNumber = row.getLong(0)
        }
        return totalNumber
    }
}
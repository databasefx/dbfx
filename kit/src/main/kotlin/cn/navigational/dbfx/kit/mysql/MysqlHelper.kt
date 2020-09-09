package cn.navigational.dbfx.kit.mysql

import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet

class MysqlHelper {
    companion object {
        fun getMysqlVersion(rowSet: RowSet<Row>): String {
            var version = ""
            for (row in rowSet) {
                val des = row.getString(0)
                if (des == "version") {
                    version = row.getString(1)
                    break
                }
            }
            return version
        }

        fun transTableName(category: String, table: String): String {
            return "`$category`.`$table`"
        }
    }
}
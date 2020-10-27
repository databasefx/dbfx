package cn.navigational.dbfx.kit.mysql

import cn.navigational.dbfx.kit.model.TableColumnMeta
import cn.navigational.dbfx.kit.model.TableColumnMeta.TableColumnExtraAttr
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

        /**
         *
         * Get mysql table column constrain type
         *
         */
        fun getTableConstrain(colKey: String?): Array<TableColumnMeta.ConstrainType> {
            return arrayOf(when (colKey) {
                "PRI" -> TableColumnMeta.ConstrainType.PRIMARY_KEY
                "UNI" -> TableColumnMeta.ConstrainType.UNIQUE_KEY
                else -> TableColumnMeta.ConstrainType.NONE
            })
        }

        /**
         * Get mysql table column extra attr
         */
        fun getExtraAttr(str: String?): Array<TableColumnExtraAttr> {
            return arrayOf(when (str) {
                "AUTO_INCREMENT" -> TableColumnExtraAttr.AUTO_INCREMENT
                "ON UPDATE CURRENT_TIMESTAMP" -> TableColumnExtraAttr.ON_UPDATE_CURRENT_TIMESTAMP
                else -> TableColumnExtraAttr.NONE
            })
        }
    }
}
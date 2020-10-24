package cn.navigational.dbfx.kit.postgres

import cn.navigational.dbfx.kit.model.TableColumnMeta

class PgSQLHelper {
    companion object {
        /**
         *
         * Get mysql table column constrain type
         *
         */
        fun getTableConstrain(colKey: String?): Array<TableColumnMeta.ConstrainType> {
            return arrayOf(when (colKey) {
                "PRIMARY KEY" -> TableColumnMeta.ConstrainType.PRIMARY_KEY
                "UNIQUE" -> TableColumnMeta.ConstrainType.UNIQUE_KEY
                "FOREIGN KEY" -> TableColumnMeta.ConstrainType.FOREIGN_KEY
                else -> TableColumnMeta.ConstrainType.NONE
            })
        }
    }
}
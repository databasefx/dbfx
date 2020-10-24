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
                "p" -> TableColumnMeta.ConstrainType.PRIMARY_KEY
                "u" -> TableColumnMeta.ConstrainType.UNIQUE_KEY
                "f" -> TableColumnMeta.ConstrainType.FOREIGN_KEY
                else -> TableColumnMeta.ConstrainType.NONE
            })
        }
    }
}
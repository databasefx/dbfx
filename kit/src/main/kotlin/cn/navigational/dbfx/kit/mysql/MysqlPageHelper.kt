package cn.navigational.dbfx.kit.mysql

class MysqlPageHelper {
    companion object {
        /**
         * Get paging start offset
         * @param pageIndex Paging start index
         * @param pageSize Paging size
         */
        fun getStartNum(pageIndex: Int, pageSize: Int): Int {
            return (pageIndex - 1) * pageSize
        }
    }
}
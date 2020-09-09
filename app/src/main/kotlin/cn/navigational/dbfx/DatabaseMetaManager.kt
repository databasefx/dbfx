package cn.navigational.dbfx

import cn.navigational.dbfx.model.DatabaseMeta

/**
 *
 *Current support sql client meta data manager
 *
 * @author yangkui
 * @since 1.0
 */
class DatabaseMetaManager {

    private val metas: ArrayList<DatabaseMeta> = arrayListOf()

    companion object {
        private val manager = DatabaseMetaManager()

        fun addDbMeta(ms: ArrayList<DatabaseMeta>) {
            manager.metas.addAll(ms)
        }

        fun getMetas(): List<DatabaseMeta> {
            return manager.metas
        }
    }
}
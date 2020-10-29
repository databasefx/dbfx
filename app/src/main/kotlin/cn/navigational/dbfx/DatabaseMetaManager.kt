package cn.navigational.dbfx

import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.model.DatabaseMeta

/**
 *
 *Current support sql client meta data manager
 *
 * @author yangkui
 * @since 1.0
 */
class DatabaseMetaManager {
    companion object {
        private val metas: ArrayList<DatabaseMeta> = arrayListOf()

        /**
         *
         * Get origin database meta
         * @param client Current client
         */
        fun getDbMeta(client: Clients): DatabaseMeta {
            for (meta in metas) {
                val cl = Clients.getClient(meta.name)
                if (cl == client) {
                    return meta
                }
            }
            throw RuntimeException("Not found $client database meta!")
        }

        fun addDbMeta(ms: ArrayList<DatabaseMeta>) {
            this.metas.addAll(ms)
        }

        fun getMetas(): List<DatabaseMeta> {
            return this.metas
        }
    }
}
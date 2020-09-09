package cn.navigational.dbfx.kit.test

import io.vertx.core.Vertx
import io.vertx.ext.unit.TestContext
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.PoolOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before

open class MySqlTestBase : BaseTest() {

    @Before
    fun testBefore(context: TestContext) {
        this.connectionOptions = MySQLConnectOptions()
                .setUser("root")
                .setPassword("root")
                .setCharset("utf8")
        this.poolOptions = PoolOptions().setMaxSize(10).setMaxWaitQueueSize(100)
        this.vertx = Vertx.vertx()
        val async = context.async()
        GlobalScope.launch {
            client = MySQLPool.pool(vertx, connectionOptions as MySQLConnectOptions, poolOptions)
            async.complete()
        }
    }
}
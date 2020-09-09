package cn.navigational.dbfx.kit.test

import io.vertx.core.Vertx
import io.vertx.ext.unit.TestContext
import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.PoolOptions
import org.junit.Before

open class PostgresqlBaseTest : BaseTest() {
    @Before
    fun testBefore(context: TestContext) {
        this.vertx = Vertx.vertx()
        this.connectionOptions = PgConnectOptions()
                .setUser("postgres")
                .setPassword("postgres")
                .setDatabase("postgres")
                .setHost("localhost")
        this.poolOptions = PoolOptions().setMaxSize(10).setMaxWaitQueueSize(100)

        client = PgPool.pool(vertx, connectionOptions as PgConnectOptions, poolOptions)
    }

}
package cn.navigational.dbfx.kit.test

import io.vertx.core.Vertx
import io.vertx.ext.unit.TestContext
import io.vertx.kotlin.coroutines.await
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlConnectOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.After

open class BaseTest {
    protected lateinit var vertx: Vertx
    protected lateinit var client: Pool
    protected lateinit var poolOptions: PoolOptions
    protected lateinit var connectionOptions: SqlConnectOptions


    @After
    fun testAfter(context: TestContext) {
        val async = context.async()
        GlobalScope.launch {
            client.close().await()
            vertx.close().await()
            async.complete()
        }
    }

}
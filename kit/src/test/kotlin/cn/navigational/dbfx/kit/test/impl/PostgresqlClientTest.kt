package cn.navigational.dbfx.kit.test.impl


import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.test.PostgresqlBaseTest
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(VertxUnitRunner::class)
class PostgresqlClientTest : PostgresqlBaseTest() {
    @Test
    fun `test connection`(context: TestContext) {
        val async = context.async()
        GlobalScope.launch {
            val res = SQLQuery.getClQuery(Clients.POSTGRESQL).showDbInfo(client)
            context.assertTrue(res.size() > 0)
            async.complete()
        }
    }
}
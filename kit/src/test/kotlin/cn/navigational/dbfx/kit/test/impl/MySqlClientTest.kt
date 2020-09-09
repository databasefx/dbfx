package cn.navigational.dbfx.kit.test.impl

import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.test.MySqlTestBase
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(VertxUnitRunner::class)
class MySqlClientTest : MySqlTestBase() {

    @Test
    fun `test connection`(context: TestContext) {
        val async = context.async()
        GlobalScope.launch {
            val rs = SQLQuery.getClQuery(Clients.MYSQL).showDbInfo(client)
            context.assertTrue(rs.size() > 0)
            async.complete()
        }
    }
}
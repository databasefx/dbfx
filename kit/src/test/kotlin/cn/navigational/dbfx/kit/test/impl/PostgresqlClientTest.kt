package cn.navigational.dbfx.kit.test.impl


import cn.navigational.dbfx.kit.SQLQuery
import cn.navigational.dbfx.kit.enums.Clients
import cn.navigational.dbfx.kit.test.PostgresqlBaseTest
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
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

    @Test(timeout = 1000)
    fun `test delete role`(context: TestContext) {
        val async = context.async()
        GlobalScope.launch(vertx.dispatcher()) {
            val sql = "DROP ROLE IF EXISTS vertx_pg_user"
            val res = client.query(sql).execute().await()
            context.assertTrue(res.rowCount() > 0)
            async.complete()
        }
    }

    /**
     *
     *This test method is used to reproduce the problem that vertx PG client cannot correctly
     *resolve some specified values of PG database time and date,Relation pg document please visit:
     * <a href='https://www.postgresql.org/docs/12/datatype-datetime.html#DATATYPE-DATETIME-SPECIAL-TABLE'/>
     * </p>
     *
     */
    @Test(timeout = 2000)
    fun `test create user`(context: TestContext) {
        val async = context.async()
        GlobalScope.launch(vertx.dispatcher()) {
            val sql = "CREATE ROLE vertx_pg_user LOGIN PASSWORD 'test' VALID UNTIL 'infinity'"
            val res = client.query(sql).execute().await()
            context.assertTrue(res.rowCount() > 0)
            val qSql = "SELECT * FROM pg_roles r WHERE r.rolname='vertx_pg_user'"
            val res1 = client.query(qSql).execute().await()
            context.assertTrue(res1.size() == 1)
            async.complete()
        }
    }

}
package cn.navigational.dbfx.kit;

import cn.navigational.dbfx.kit.enums.Clients;
import cn.navigational.dbfx.kit.ex.NotSupportException;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.SqlConnectOptions;

/**
 * Sql client factory
 *
 * @author yangkui
 * @since 1.0
 */
public interface SqlClientFactory {
    /**
     * Default pool options
     */
    PoolOptions DEFAULT_POOL_OPTIONS = new PoolOptions().setMaxSize(1).setMaxWaitQueueSize(10);

    /**
     * Create a sql client
     *
     * @param vertx          {@link Vertx} instance
     * @param cl             Current support sql client type
     * @param connectOptions Sql connection options
     * @param poolOptions    Connection pool options
     * @return {@link Clients} for sql client
     * @author YangKui
     * @since 1.0
     */
    static Pool createClient(Vertx vertx, SqlConnectOptions connectOptions, PoolOptions poolOptions, Clients cl) {
        final Pool client;
        if (cl == Clients.MYSQL) {
            client = MySQLPool.pool(vertx, (MySQLConnectOptions) connectOptions, poolOptions);
        } else if (cl == Clients.POSTGRESQL) {
            client = PgPool.pool(vertx, (PgConnectOptions) connectOptions, poolOptions);
        } else {
            throw new NotSupportException("Target database [" + cl + "] not supported.");
        }
        return client;
    }

    /**
     * Create a sql client use default pool options
     *
     * @param vertx          vertx instance
     * @param connectOptions Connection options
     * @param cl             {@link Clients}
     * @return {@link Clients} for sql client
     */
    static SqlClient createClient(Vertx vertx, SqlConnectOptions connectOptions, Clients cl) {
        final SqlClient client;
        if (cl == Clients.MYSQL) {
            client = MySQLPool.pool(vertx, (MySQLConnectOptions) connectOptions, DEFAULT_POOL_OPTIONS);
        } else if (cl == Clients.POSTGRESQL) {
            client = PgPool.pool(vertx, (PgConnectOptions) connectOptions, DEFAULT_POOL_OPTIONS);
        } else {
            throw new NotSupportException("Target database [" + cl + "] not supported.");
        }
        return client;
    }

    /**
     * According {@link Clients} to create {@link SqlConnectOptions} object
     *
     * @param cl Target cl
     * @return {@link SqlConnectOptions}
     */
    static SqlConnectOptions createConnectionOptions(Clients cl) {
        final SqlConnectOptions options;
        if (cl == Clients.MYSQL) {
            options = new MySQLConnectOptions();
        } else if (cl == Clients.POSTGRESQL) {
            options = new PgConnectOptions();
        } else {
            throw new NotSupportException("Target database [" + cl + "] not supported.");
        }
        return options;
    }
}

package cn.navigational.dbfx.model;

import cn.navigational.dbfx.kit.enums.Clients;
import io.vertx.sqlclient.Pool;

/**
 * For running sql client package
 *
 * @author yangkui
 * @since 1.0
 */
public class SQLClient {
    /**
     * Current client id
     */
    private String uuid;
    /**
     * Current database info
     */
    private DbInfo dbInfo;
    /**
     * Sql client info
     */
    private Pool client;
    /**
     * Current database version
     */
    private String version;
    /**
     * {@link Clients}
     */
    private Clients cl;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public DbInfo getDbInfo() {
        return dbInfo;
    }

    public void setDbInfo(DbInfo dbInfo) {
        this.dbInfo = dbInfo;
    }

    public Pool getClient() {
        return client;
    }

    public void setClient(Pool client) {
        this.client = client;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Clients getCl() {
        return cl;
    }

    public void setCl(Clients cl) {
        this.cl = cl;
    }
}

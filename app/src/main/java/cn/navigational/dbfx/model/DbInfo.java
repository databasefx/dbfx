package cn.navigational.dbfx.model;

import cn.navigational.dbfx.kit.enums.Clients;

/**
 * @author yangkui
 * @since 1.0
 */
public class DbInfo {
    /**
     * Current database unique identification
     */
    private String uuid;
    /**
     * {@link Clients}
     */
    private Clients client;
    /**
     * Connection name
     */
    private String name;
    /**
     * Connection comment
     */
    private String comment;
    /**
     * Connection host
     */
    private String host;
    /**
     * Username
     */
    private String username;
    /**
     * Connection password
     */
    private String password;
    /**
     * Connection database
     */
    private String database;
    /**
     * Connection port
     */
    private Integer port;
    /**
     * Is local storage
     */
    private Boolean local;

    public Clients getClient() {
        return client;
    }

    /**
     * Update current database instance every field value
     *
     * @param info {@link DbInfo}
     */
    public void updateField(DbInfo info) {
        this.name = info.name;
        this.host = info.host;
        this.port = info.port;
        this.uuid = info.uuid;
        this.local = info.local;
        this.client = info.client;
        this.username = info.username;
        this.password = info.password;
        this.database = info.database;
        this.comment = info.comment;
    }

    public void setClient(Clients client) {
        this.client = client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getLocal() {
        return local;
    }

    public void setLocal(Boolean local) {
        this.local = local;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "DbInfo{" +
                "client=" + client +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", host='" + host + '\'' +
                ", password='" + password + '\'' +
                ", database='" + database + '\'' +
                ", port=" + port +
                ", local=" + local +
                '}';
    }
}

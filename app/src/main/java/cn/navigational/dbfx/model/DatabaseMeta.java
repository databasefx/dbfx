package cn.navigational.dbfx.model;

/**
 * Database meta data
 *
 * @author yangkui
 * @since 1.0
 */
public class DatabaseMeta {
    /**
     * DB Name
     */
    private String name;
    /**
     * Current is support
     */
    private Boolean support;
    /**
     * DB icon
     */
    private String icon;
    /**
     * DB default port
     */
    private Integer port;
    /**
     * Default select database
     */
    private String database;
    /**
     * Default username
     */
    private String username;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSupport() {
        return support;
    }

    public void setSupport(Boolean support) {
        this.support = support;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

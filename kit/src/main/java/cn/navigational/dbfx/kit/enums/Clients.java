package cn.navigational.dbfx.kit.enums;

/**
 * Enum current support database type
 *
 * @author yangkui
 * @since 1.0
 */
public enum Clients {
    /**
     * Mysql client
     */
    MYSQL("MySQL"),
    /**
     * Postgresql client
     */
    POSTGRESQL("PostgreSQL"),
    /**
     * Microsoft SQL Serve
     */
    MS_SQL("SqlServer"),
    /**
     * Db2 client
     */
    DB2("DB2");

    private final String client;

    Clients(String client) {
        this.client = client;
    }

    public String getClient() {
        return client;
    }

    public static Clients getClient(String value) {
        final Clients client;
        if (value.equals(MS_SQL.client)) {
            client = MS_SQL;
        } else if (value.equals(MYSQL.client)) {
            client = MYSQL;
        } else if (value.equals(POSTGRESQL.client)) {
            client = POSTGRESQL;
        } else {
            client = DB2;
        }
        return client;
    }
}

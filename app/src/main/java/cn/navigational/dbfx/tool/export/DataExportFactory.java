package cn.navigational.dbfx.tool.export;

/**
 * Data export factory
 *
 * @author yangkui
 * @since 1.0
 */
public class DataExportFactory {
    public enum ExportFormat {
        /**
         * Json file
         */
        JSON("JSON", "json"),
        /**
         * Excel file
         */
        EXCEL("Excel", "xlsx"),
        /**
         * XML file
         */
        XML("XML", "xml"),
        /**
         * CSV file
         */
        CSV("CSV", "csv"),
        /**
         * Sql inserts file
         */
        SQL_INSERT("SQL Inserts", "sql"),
        /**
         * Sql update file
         */
        SQL_UPDATE("SQL Updates", "sql");
        /**
         * Format name
         */
        private final String name;
        /**
         * File suffix format
         */
        private final String format;

        ExportFormat(String name, String format) {
            this.name = name;
            this.format = format;
        }

        public String getName() {
            return name;
        }

        public String getFormat() {
            return format;
        }
    }

    public static ExportFormat getExFormat(String f) {
        ExportFormat format = null;
        for (ExportFormat value : ExportFormat.values()) {
            if (f.equals(value.name)) {
                format = value;
                break;
            }
        }
        return format;
    }

    public static String getFormat(String f) {
        var ex = getExFormat(f);
        return ex.format;
    }
}

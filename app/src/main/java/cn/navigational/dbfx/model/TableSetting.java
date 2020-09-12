package cn.navigational.dbfx.model;

import static cn.navigational.dbfx.kit.config.ConstantsKt.NULL_VALUE;

/**
 * Table setting
 *
 * @author yangkui
 * @since 1.0
 */
public class TableSetting {
    /**
     * Global save?
     */
    private boolean global = false;
    /**
     * Default value show value
     */
    private String nulFormat = NULL_VALUE;
    /**
     * Default date time format pattern
     */
    private String dtFormat = "yyyy-MM-dd HH:mm:ss";

    public String getDtFormat() {
        return dtFormat;
    }

    public void setDtFormat(String dtFormat) {
        this.dtFormat = dtFormat;
    }

    public String getNulFormat() {
        return nulFormat;
    }

    public void setNulFormat(String nulFormat) {
        this.nulFormat = nulFormat;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }
}

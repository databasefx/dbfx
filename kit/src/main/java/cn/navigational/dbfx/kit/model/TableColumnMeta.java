package cn.navigational.dbfx.kit.model;

import cn.navigational.dbfx.kit.enums.DataType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Database Table column meta data
 *
 * @author yangkui
 * @since 1.0
 */
public class TableColumnMeta {
    /**
     * Column name
     */
    private String colName;
    /**
     * Column data length
     */
    private Integer length;
    /**
     * Column data type
     */
    private String type;
    /**
     * That column is key?
     */
    private Boolean key;
    /**
     * That column comment
     */
    private String comment;
    /**
     * Data type
     */
    private DataType dataType;
    /**
     * Column position
     */
    private Integer position;
    /**
     * Column null?
     */
    private boolean nullable;
    /**
     * Current column constrain types
     */
    private ConstrainType[] constrainTypes;
    /**
     * Current column extra attributes
     */
    private TableColumnExtraAttr[] extraAttr;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = Objects.requireNonNullElse(length, 0);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getKey() {
        return key;
    }

    public void setKey(Boolean key) {
        this.key = key;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Integer getPosition() {
        return position;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public ConstrainType[] getConstrainTypes() {
        return constrainTypes;
    }

    public void setConstrainTypes(ConstrainType[] constrainTypes) {
        this.constrainTypes = constrainTypes;
    }

    public TableColumnExtraAttr[] getExtraAttr() {
        return extraAttr;
    }

    public void setExtraAttr(TableColumnExtraAttr[] extraAttr) {
        this.extraAttr = extraAttr;
    }

    /**
     * This method is called when there are only table column names and no more column metadata to create a default table raw data Java object mapping
     *
     * @param columnNames Table column name
     * @return {@link TableColumnMeta} Table column meta
     */
    public static List<TableColumnMeta> createDefaultTableColumnMeta(List<String> columnNames) {
        return columnNames.stream().map(it -> {
            var meta = new TableColumnMeta();
            meta.setLength(0);
            meta.setColName(it);
            meta.setDataType(DataType.STRING);
            meta.setConstrainTypes(new ConstrainType[]{ConstrainType.NONE});
            meta.setExtraAttr(new TableColumnExtraAttr[]{TableColumnExtraAttr.NONE});
            return meta;
        }).collect(Collectors.toList());
    }

    /**
     * Database table column constrain type
     */
    public enum ConstrainType {
        /**
         * No any constrain
         */
        NONE,
        /**
         * Primary key
         */
        PRIMARY_KEY,
        /**
         * Unique key
         */
        UNIQUE_KEY,
        /**
         * Foreign key constraint
         */
        FOREIGN_KEY
    }

    /**
     * Current table column extra attribute
     */
    public enum TableColumnExtraAttr {
        /**
         * No any extra attr
         */
        NONE,
        /**
         * Auto-increment
         */
        AUTO_INCREMENT,
        /**
         * Auto update timestamp or datetime
         */
        ON_UPDATE_CURRENT_TIMESTAMP
    }
}

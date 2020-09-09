package cn.navigational.dbfx.kit.model;

import cn.navigational.dbfx.kit.enums.DataType;

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
        this.length = length;
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

    public void setPosition(Integer position) {
        this.position = position;
    }
}

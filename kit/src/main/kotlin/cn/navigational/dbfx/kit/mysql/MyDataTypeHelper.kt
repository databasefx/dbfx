package cn.navigational.dbfx.kit.mysql

import cn.navigational.dbfx.kit.enums.DataType

private val number: Array<String> = arrayOf(
        "TINYINT", "SMALLINT", "MEDIUMINT", "INT", "INTEGER", "BIGINT", "FLOAT", "DOUBLE", "DECIMAL")
private val dateTime: Array<String> = arrayOf("DATE", "TIME", "YEAR", "DATETIME", "TIMESTAMP")
//private val string: Array<String> = arrayOf("CHAR", "VARCHAR", "TINYBLOB", "TINYTEXT", "BLOB",
//        "TEXT", "MEDIUMBLOB", "MEDIUMTEXT", "LONGBLOB", "LONGTEXT")

fun getMyDataType(type: String): DataType {
    val temp = type.toUpperCase()
    return when {
        number.contains(temp) -> {
            DataType.NUMBER
        }
        dateTime.contains(temp) -> {
            DataType.DATE
        }
        else -> {
            DataType.STRING
        }
    }
}
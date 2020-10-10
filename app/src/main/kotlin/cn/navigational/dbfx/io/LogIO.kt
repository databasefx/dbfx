package cn.navigational.dbfx.io

import cn.navigational.dbfx.kit.utils.OssUtils
import java.io.File
import java.io.FileReader
import java.io.LineNumberReader
import java.lang.StringBuilder

//application log path
val APP_LOG_PATH = OssUtils.getUserHome() + File.separator + ".dbfx" + File.separator + "logs" + File.separator

fun countLogLineNumber(path: String): Int {
    val file = File(path)
    if (!file.exists()) {
        return -1
    }
    val lines: Int
    var lineNumberReader: LineNumberReader? = null
    val length = file.length()
    try {
        lineNumberReader = LineNumberReader(FileReader(file))
        lineNumberReader.skip(length)
        lines = lineNumberReader.lineNumber
    } finally {
        lineNumberReader?.close()
    }
    return lines
}

fun readFixLineLog(path: String, start: Int, end: Int): String {
    val file = File(path) //文件路径

    val fileReader = FileReader(file)
    val reader = LineNumberReader(fileReader)
    var str: String?
    var lines = 0
    val sb = StringBuilder()
    while (true) {
        lines++
        str = reader.readLine()
        if (str == null) {
            break
        }
        if (lines in start..end) {
            sb.append(str)
            sb.append("\r\n")
        }
    }
    return sb.toString();
}
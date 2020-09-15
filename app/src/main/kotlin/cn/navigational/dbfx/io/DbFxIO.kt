package cn.navigational.dbfx.io

import cn.navigational.dbfx.DatabaseMetaManager
import cn.navigational.dbfx.Launcher
import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.kit.config.PASSWORD
import cn.navigational.dbfx.model.DatabaseMeta
import cn.navigational.dbfx.model.DbInfo
import cn.navigational.dbfx.security.AseAlgorithm
import cn.navigational.dbfx.kit.utils.OssUtils
import cn.navigational.dbfx.kit.utils.StringUtils
import cn.navigational.dbfx.kit.utils.VertxUtils
import cn.navigational.dbfx.model.UiPreferences
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

const val DEFAULT_KEY = "2EBC@#=="
const val S_DB_PATH = "config/s_db.json"

val APP_HOME = OssUtils.getUserHome() + File.separator + ".dbfx"

/**
 * Database config file
 */
val dbInfoPath = "$APP_HOME/db_preference.json"

/**
 * Application ui config file
 * <note>
 *     When the external configuration file does not exist and cannot be loaded,
 *     the default UI configuration information is used.
 * </note>
 */
val uiConfigPath = "$APP_HOME/ui_preferences.json"

/**
 * Default ui config file path
 */
const val defaultUiConfigPath = "config/ui_preferences.json"

/**
 *
 * Update DbInfo list to local file.
 *
 * @param list  Database info list
 *
 */
fun updateDbInfoList(list: List<DbInfo>) {
    GlobalScope.launch {
        val array = JsonArray()
        val fs = VertxUtils.getFileSystem()
        for (info in list) {
            //If current database info not need local cached skip.
            if (!info.local) {
                continue
            }
            val json = JsonObject.mapFrom(info)
            val password = json.getString(PASSWORD)
            if (StringUtils.isNotEmpty(password)) {
                val aseStr = AseAlgorithm.encrypt(password, DEFAULT_KEY)
                json.put(PASSWORD, aseStr)
            }
            array.add(json)
        }
        fs.writeFile(dbInfoPath, array.toBuffer())
    }
}

/**
 *
 * Get db_info.json file content
 *
 */
private suspend fun getDbFile(): JsonArray {
    val fs = VertxUtils.getFileSystem()
    val array: JsonArray
    if (fs.existsBlocking(dbInfoPath)) {
        array = fs.readFile(dbInfoPath).await().toJsonArray()
    } else {
        array = JsonArray()
        fs.createFile(dbInfoPath).await()
        fs.writeFile(dbInfoPath, array.toBuffer())
    }
    return array
}

/**
 *
 * Check project need direction is exist
 *
 */
private suspend fun checkDir() {
    val fs = VertxUtils.getFileSystem()
    val exist = fs.exists(APP_HOME).await()
    if (exist) {
        return
    }
    fs.mkdirs(APP_HOME).await()
}

/**
 *
 * Load local cached database info
 *
 */
private suspend fun loadDbInfo() {
    val array = getDbFile()
    for (item in array.stream().map { it as JsonObject }) {
        val info = item.mapTo(DbInfo::class.java)
        if (StringUtils.isNotEmpty(info.password)) {
            val deStr = AseAlgorithm.decrypt(info.password, DEFAULT_KEY)
            info.password = deStr
        }
        SQLClientManager.manager.getDbInfo().add(info)
    }
}

/**
 * Load local cached ui config file
 */
private suspend fun loadUiConfig() {
    val fs = VertxUtils.getFileSystem()
    try {
        val json = fs.readFile(uiConfigPath).await().toJsonObject()
        Launcher.uiPreference = json.mapTo(UiPreferences::class.java)
    } catch (e: Exception) {
        println("Load UI config failed:[${e.message}],so use default UI config")
        val json = fs.readFile(defaultUiConfigPath).await().toJsonObject()
        Launcher.uiPreference = json.mapTo(UiPreferences::class.java)
        //use default UI preference override block UI preference file
        flushUiPreference()
    }
}

/**
 * Flush UI Preference to local cached
 */
fun flushUiPreference() {
    val fs = VertxUtils.getFileSystem()
    val json = JsonObject.mapFrom(Launcher.uiPreference)

    val future = fs.writeFile(uiConfigPath, json.toBuffer())
    future.onComplete {
        if (it.succeeded()) {
            return@onComplete
        }
        println("Flush UI preference failed cause:[${it.cause().message}")
    }
}

/**
 *
 * Load local s_db.json file into memcached
 *
 */
private suspend fun loadDbMetaData() {
    val fs = VertxUtils.getFileSystem()
    val array = fs.readFile(S_DB_PATH).await().toJsonArray()
    val items = arrayListOf<DatabaseMeta>()
    array.forEach {
        val json = it as JsonObject
        items.add(json.mapTo(DatabaseMeta::class.java))
    }
    DatabaseMetaManager.manager.addDbMeta(items)
}

/**
 *
 *Init project io
 *
 */
suspend fun init() {
    checkDir()
    loadDbInfo()
    loadUiConfig()
    loadDbMetaData()
}
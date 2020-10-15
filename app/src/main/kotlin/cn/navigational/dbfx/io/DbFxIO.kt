package cn.navigational.dbfx.io

import cn.navigational.dbfx.DatabaseMetaManager
import cn.navigational.dbfx.SQLClientManager
import cn.navigational.dbfx.kit.config.FILE
import cn.navigational.dbfx.kit.config.PASSWORD
import cn.navigational.dbfx.kit.utils.StringUtils
import cn.navigational.dbfx.kit.utils.VertxUtils
import cn.navigational.dbfx.model.DatabaseMeta
import cn.navigational.dbfx.model.DbInfo
import cn.navigational.dbfx.security.AseAlgorithm
import cn.navigational.dbfx.utils.AppSettings
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.coroutines.await
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.io.File
import java.net.JarURLConnection

import java.util.*
import java.util.jar.JarFile.MANIFEST_NAME
import cn.navigational.dbfx.AppPlatform
import cn.navigational.dbfx.model.Manifest


const val DEFAULT_KEY = "2EBC@#=="
const val S_DB_PATH = "config/s_db.json"

val APP_HOME = AppPlatform.getApplicationDataFolder()

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
 * slf4j logger
 */
private val logger = LoggerFactory.getLogger("DbFxIO")

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
        AppSettings.setAppSettings(json.mapTo(AppSettings::class.java))
    } catch (e: Exception) {
        logger.info("Load UI config failed:[{}],so use default UI config", e.message)
        val json = fs.readFile(defaultUiConfigPath).await().toJsonObject()
        AppSettings.setAppSettings(json.mapTo(AppSettings::class.java))
//        use default UI preference override block UI preference file
        flushUiPreference()
    }
}

/**
 * Flush UI Preference to local cached
 */
fun flushUiPreference() {
    val fs = VertxUtils.getFileSystem()
    val json = JsonObject.mapFrom(AppSettings.getAppSettings())

    val future = fs.writeFile(uiConfigPath, json.toBuffer())
    future.onComplete {
        if (it.succeeded()) {
            return@onComplete
        }
        logger.debug("Flush UI preference failed cause:[{}]", it.cause().message)
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
        //filter not support client
        if (!json.getBoolean("support")) {
            return@forEach
        }
        items.add(json.mapTo(DatabaseMeta::class.java))
    }
    DatabaseMetaManager.manager.addDbMeta(items)
}

/**
 * load java MANIFEST.MF file
 *
 * @return Key value pair formal attribute
 */
fun loadManifest() {
    val protocol = ClassLoader.getSystemResource("").protocol
    val map = HashMap<String, String>()
    if (FILE == protocol) {
        //Gradle temp path
        val buildPath = "build/tmp/jar/MANIFEST.MF"
        val mf = VertxUtils.getFileSystem().readFileBlocking(buildPath).toString()
        val array = mf.split("\r\n")
        for (s in array) {
            if (s.isEmpty) {
                continue
            }
            val k = s.split(":", limit = 2).toTypedArray()
            map[k[0].trim()] = k[1].trim()
        }
    } else {
        val url = ClassLoader.getSystemResource(MANIFEST_NAME)
        val jarCon: JarURLConnection = url.openConnection() as JarURLConnection
        val attrs = jarCon.mainAttributes
        for ((key, value) in attrs) {
            map[key.toString()] = value.toString()
        }
    }
    val manifest = Manifest()
    manifest.name = map["App-Name"]
    manifest.author = map["App-Author"]
    manifest.website = map["App-Website"]
    manifest.version = map["App-Version"]
    manifest.copyright = map["Copyright"]
    manifest.buildTime = map["Build-Time"]

    AppSettings.getAppSettings().manifest = manifest
}

/**
 *
 * Get fix file last modify time,if file not exist return -1
 */
fun getFileLastTime(path: String): Long {
    val file = File(path)
    if (!file.exists()) {
        return -1
    }
    return file.lastModified()
}

/**
 * Output application banner info
 */
private suspend fun outputBan() {
    val fs = VertxUtils.getFileSystem()
    val buffer = fs.readFile("banner.txt").await()
    val bannerTxt = buffer.toString()
    logger.info("\r\n{}", bannerTxt)
}

/**
 *
 *Init project io
 *
 */
suspend fun init() {
    outputBan()
    checkDir()
    loadDbInfo()
    loadUiConfig()
    loadManifest()
    loadDbMetaData()
}
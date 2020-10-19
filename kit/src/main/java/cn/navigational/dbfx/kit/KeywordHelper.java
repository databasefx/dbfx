package cn.navigational.dbfx.kit;

import cn.navigational.dbfx.kit.enums.Clients;
import cn.navigational.dbfx.kit.utils.VertxUtils;
import io.vertx.core.Future;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonArray;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SQL Statement keyword helper
 *
 * @author yangkui
 * @since 1.0
 */
public class KeywordHelper {
    private static final String K_PATH = "config/keyword/";
    private static final List<String> SUPPORT_KEYWORD = new ArrayList<>();

    static {
        final String supportPath = K_PATH + "support_key.json";
        final FileSystem fs = VertxUtils.getFileSystem();
        final JsonArray array = fs.readFileBlocking(supportPath).toJsonArray();
        SUPPORT_KEYWORD.addAll(array.stream().map(Object::toString).collect(Collectors.toList()));
    }

    public static List<String> getKeyWordSyn(Clients cl) {
        return SUPPORT_KEYWORD;
    }

    public static Future<List<String>> getKeyWordAsync(Clients cl) {
        return Future.succeededFuture(SUPPORT_KEYWORD);
    }
}

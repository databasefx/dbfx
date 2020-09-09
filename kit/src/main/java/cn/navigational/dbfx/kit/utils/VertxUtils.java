package cn.navigational.dbfx.kit.utils;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.file.FileSystem;

/**
 * Vertx utils
 *
 * @author yangkui
 * @since 1.0
 */
public class VertxUtils {

    private static Vertx vertx;

    private volatile static boolean init = false;

    public static synchronized Vertx initVertx(VertxOptions options) {
        if (init) {
            throw new RuntimeException("Vertx only init once.");
        }
        if (options == null) {
            vertx = Vertx.vertx();
        } else {
            vertx = Vertx.vertx(options);
        }
        init = true;
        return vertx;
    }

    public static <T> MessageConsumer<T> eventBusConsumer(String addr) {
        if (!init) {
            initVertx(null);
        }
        return vertx.eventBus().<T>consumer(addr);
    }

    public static void unRegisterEventBus(String addr) {
        if (!init) {
            initVertx(null);
        }
        vertx.eventBus().unregisterCodec(addr);
    }

    public static <T> Future<Message<T>> sendEventBus(String addr, T msg) {
        if (!init) {
            initVertx(null);
        }
        return vertx.eventBus().<T>request(addr, msg);
    }


    public static Vertx getVertx() {
        final Vertx vertex;
        if (!init) {
            vertex = initVertx(null);
        } else {
            vertex = vertx;
        }
        return vertex;
    }

    public static void close() {
        if (!init) {
            return;
        }
        vertx.close(ar -> {
            if (ar.failed()) {
                System.out.println("Close vertex failed:[" + ar.cause().getMessage() + "]");
            } else {
                System.out.println("Success close vertex.");
            }
        });
    }

    public static FileSystem getFileSystem() {
        return getVertx().fileSystem();
    }

}
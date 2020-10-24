package cn.navigational.dbfx.kit.utils;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.FileSystemOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Vertx utils
 *
 * @author yangkui
 * @since 1.0
 */
public class VertxUtils {
    private static Vertx vertx;

    private static final Logger LOG = LoggerFactory.getLogger(VertxUtils.class);

    /**
     * Register EventBus address
     *
     * @param addr EB address
     * @param <T>  Message type
     * @return Consumer handler
     */
    public static <T> MessageConsumer<T> eventBusConsumer(String addr) {
        return getVertx().eventBus().<T>consumer(addr);
    }

    /**
     * Un-Register EventBus
     *
     * @param addr EB address
     */
    public static void unRegisterEventBus(String addr) {
        getVertx().eventBus().unregisterCodec(addr);
    }

    /**
     * Send event bus message
     *
     * @param addr Target eb address
     * @param msg  message
     * @param <T>  Message type
     * @return Send result
     */
    public static <T> Future<Message<T>> sendEventBus(String addr, T msg) {
        return getVertx().eventBus().<T>request(addr, msg);
    }

    /**
     * Get vertx instance
     *
     * @return Vertx instance
     */
    public static synchronized Vertx getVertx() {
        if (vertx == null) {
            LOG.debug("Start init vertx options.");
            var vOptions = new VertxOptions()
                    .setWorkerPoolSize(10)
                    .setMaxWorkerExecuteTime(5)
                    .setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS)
                    .setFileSystemOptions(new FileSystemOptions().setFileCachingEnabled(false));

            vertx = Vertx.vertx(vOptions);
        }
        return vertx;
    }

    /**
     * Close current vertx instance occur resource
     */
    public static void close() {
        getVertx().close(ar -> {
            if (ar.failed()) {
                LOG.error("Close vertex failed", ar.cause());
            } else {
                LOG.debug("Success close vertex.");
            }
        });
    }

    /**
     * Get current vertx belong {@link FileSystem}
     *
     * @return {@link FileSystem} instance
     */
    public static FileSystem getFileSystem() {
        return getVertx().fileSystem();
    }

}
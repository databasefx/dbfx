package cn.navigational.dbfx;

import cn.navigational.dbfx.kit.utils.VertxUtils;
import cn.navigational.dbfx.model.UiPreferences;
import cn.navigational.dbfx.view.SplashView;
import io.vertx.core.VertxOptions;
import io.vertx.core.file.FileSystemOptions;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.ServerSocket;


/**
 * Javafx start class
 *
 * @author yangkui
 * @since 1.0
 */
public class Launcher extends Application {
    /**
     * Application default occur port
     */
    private static final int PORT = 9999;
    /**
     * Log
     */
    private final static Logger LOG = LoggerFactory.getLogger(Launcher.class);

    /**
     * Global UI preference
     */
    public static UiPreferences uiPreference;

    @Override
    public void init() {
        if (!checkIfRunning()) {
            Platform.exit();
            return;
        }
        LOG.debug("Start init vertx options.");
        var vertxOptions = new VertxOptions();
        var fsOptions = new FileSystemOptions();
        //Disable file cached
        fsOptions.setFileCachingEnabled(false);
        //Worker pool size
        vertxOptions.setWorkerPoolSize(10);
        vertxOptions.setFileSystemOptions(fsOptions);
        VertxUtils.initVertx(vertxOptions);
    }

    @Override
    public void start(Stage primaryStage) {
        new SplashView();
    }

    @Override
    public void stop() throws Exception {
        LOG.debug("Stop current application.");
        super.stop();
        VertxUtils.close();
    }

    /**
     * Determine whether the current application has been started
     *
     * @return If current application not startup return {false} otherwise return {true}
     */
    private boolean checkIfRunning() {
        LOG.debug("Start check current application whether startup.");
        var result = false;
        try {
            new ServerSocket(PORT, 0, InetAddress.getByAddress(new byte[]{127, 0, 0, 1}));
            result = true;
        } catch (Exception e) {
            LOG.error("Current application instance already exist.", e);
        }
        return result;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

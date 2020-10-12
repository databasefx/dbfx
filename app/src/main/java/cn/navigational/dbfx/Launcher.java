package cn.navigational.dbfx;

import cn.navigational.dbfx.kit.utils.VertxUtils;
import cn.navigational.dbfx.model.UiPreferences;
import cn.navigational.dbfx.view.SplashView;

import javafx.application.Application;
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
    /**
     * Socket must be a static variable. If it is a local variable,
     * when the method is executed, the socket will be recycled with the death of the method stack.</p>
     */
    private static ServerSocket socket;


    /**
     * Determine whether the current application has been started
     *
     * @return If current application not startup return {false} otherwise return {true}
     */
    private static boolean checkIfRunning() {
        LOG.debug("Start check current application whether startup.");
        var result = true;
        try {
            socket = new ServerSocket(PORT, 0, InetAddress.getByAddress(new byte[]{127, 0, 0, 1}));
            result = false;
        } catch (Exception e) {
            LOG.info("Current application instance already exist.");
        }
        return result;
    }

    public static void main(String[] args) {
        if (checkIfRunning()) {
            return;
        }
        VertxUtils.initVertx();
        launch(Launcher.class, args);
    }

    private static void setApplicationUncaughtExceptionHandler() {
        if (Thread.getDefaultUncaughtExceptionHandler() == null) {
            // Register a Default Uncaught Exception Handler for the application
            Thread.setDefaultUncaughtExceptionHandler(new DbFxUncaughtExceptionHandler());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        //Global catch javafx UI un-caught  thread exception
        setApplicationUncaughtExceptionHandler();
        new SplashView();
    }

    @Override
    public void stop() throws Exception {
        LOG.debug("Stop current application.");
        VertxUtils.close();
    }

    private static class DbFxUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(DbFxUncaughtExceptionHandler.class);

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            // Print the details of the exception in dbfx log file
            LOGGER.error("An exception was thrown:", e);
        }
    }
}

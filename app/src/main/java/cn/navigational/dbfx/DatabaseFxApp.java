package cn.navigational.dbfx;

import cn.navigational.dbfx.i18n.I18N;
import cn.navigational.dbfx.kit.utils.VertxUtils;
import cn.navigational.dbfx.view.SplashViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.controlsfx.dialog.ExceptionDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Javafx application class
 *
 * @author yangkui
 * @since 1.0
 */
public class DatabaseFxApp extends Application implements AppPlatform.AppNotificationHandler {
    private final static Logger LOG = LoggerFactory.getLogger(Launcher.class);

    private static void setApplicationUncaughtExceptionHandler() {
        if (Thread.getDefaultUncaughtExceptionHandler() == null) {
            // Register a Default Uncaught Exception Handler for the application
            Thread.setDefaultUncaughtExceptionHandler(new DbFxUncaughtExceptionHandler());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        //Global catch javafx UI thread un-caught exception
        setApplicationUncaughtExceptionHandler();
        try {
            if (!AppPlatform.requestStart(this, getParameters())) {
                Platform.exit();
            }
        } catch (Exception e) {
            var error = new ExceptionDialog(e);
            error.setTitle(I18N.getString("alert.title.start"));
            error.setHeaderText(I18N.getString("alert.start.failure.message"));
            error.showAndWait();
            Platform.exit();
        }
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

    @Override
    public void handleLaunch(List<String> files) {
        new SplashViewController();
    }

    @Override
    public void handleOpenFilesAction(List<String> files) {

    }

    @Override
    public void handleMessageBoxFailure(Exception x) {

    }

    @Override
    public void handleQuitAction() {

    }
}

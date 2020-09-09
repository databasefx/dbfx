package cn.navigational.dbfx;

import cn.navigational.dbfx.kit.utils.VertxUtils;
import cn.navigational.dbfx.view.SplashView;
import io.vertx.core.VertxOptions;
import io.vertx.core.file.FileSystemOptions;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Javafx start class
 *
 * @author yangkui
 * @since 1.0
 */
public class Launcher extends Application {

    @Override
    public void init() {
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
        super.stop();
        VertxUtils.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

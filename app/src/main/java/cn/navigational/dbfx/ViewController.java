package cn.navigational.dbfx;

import cn.navigational.dbfx.tool.svg.SvgImageTranscoder;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import static cn.navigational.dbfx.config.AppConstantsKt.APP_ICON;


/**
 * This class encapsulates stage, which encapsulates some event monitoring,
 * such as display event monitoring, window minimization listening,
 * window closing event listening, etc</p>
 *
 * @param <T>
 * @author yangkui
 * @since 1.0
 */
public class ViewController<T extends Parent> extends AbstractFxmlController<T> {
    /**
     * Current stage scene
     */
    private final Scene scene;
    /**
     * Current stage instance object
     */
    private final Stage stage = new Stage();
    /**
     * Default stage icon
     */
    private static final Image ICON_IMAGE = SvgImageTranscoder.svgToImage(APP_ICON);

    public ViewController(String path) {
        super(path);
        this.scene = new Scene(getParent());
        this.stage.setScene(scene);
        this.stage.getIcons().add(ICON_IMAGE);
        this.stage.setOnCloseRequest(this::onCloseRequest);
        this.scene.getStylesheets().add("css/app_style.css");
    }


    /**
     * This method is called when the window is closed
     *
     * @param event Event source
     */
    protected void onCloseRequest(WindowEvent event) {
    }

    public void showStage() {
        this.stage.show();
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * Setting the current stage size has set the current screen size as
     * the base according to the given proportion.</p>
     *
     * @param wProp width proportion
     * @param hProp height proportion
     */
    protected void setSizeWithScreen(double wProp, double hProp) {
        var rect = Screen.getPrimary().getBounds();
        var width = rect.getWidth() * wProp;
        var height = rect.getHeight() * hProp;
        this.stage.setWidth(width);
        this.stage.setHeight(height);
    }
}

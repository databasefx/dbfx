package cn.navigational.dbfx;

import cn.navigational.dbfx.tool.svg.SvgImageTranscoder;
import cn.navigational.dbfx.kit.utils.VertxUtils;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventTarget;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import static cn.navigational.dbfx.config.AppConstantsKt.APP_ICON;


/**
 * This class encapsulates stage, which encapsulates some event monitoring,
 * such as display event monitoring, window minimization listening,
 * window closing event listening, etc</p>
 *
 * <note>
 * Note that the {@link View#onCreated(Scene)} and {@link View#onCreated(Scene, Object)} ) method takes effect only if you use the fxml layout.
 * </note>
 *
 * @param <T>
 * @author yangkui
 * @since 1.0
 */
public class View<T> extends Stage {

    private final ChangeListener<Boolean> showListener = (ob, ol, n) -> onShow(n);

    private final ChangeListener<Boolean> iconifyListener = (ob, ol, n) -> onIconify(n);
    /**
     * Default stage icon
     */
    private static final Image ICON_IMAGE = SvgImageTranscoder.svgToImage(APP_ICON);

    /**
     * Create a View use Empty construction
     */
    public View() {
    }

    /**
     * Create a view use fxml
     *
     * @param path FXML view path
     */
    public View(String path) {
        this(path, null);
    }

    /**
     * Create a view use fxml and data t
     *
     * @param path FXML view path
     * @param t    data t
     */
    public View(String path, T t) {
        dispatcher(path, t);
    }

    private void dispatcher(String path, T t) {
        var parent = FXMLHelper.<Parent>loadFxml(path, this);
        var scene = new Scene(parent);
        scene.getStylesheets().add("css/app_style.css");
        setScene(scene);
        this.getIcons().add(ICON_IMAGE);
        this.setOnCloseRequest(this::onClose);
        this.showingProperty().addListener(showListener);
        this.iconifiedProperty().addListener(iconifyListener);
        VertxUtils.eventBusConsumer(this.getClass().getName())
                .handler(msg -> Platform.runLater(() -> this.notifyCall(msg)));
        if (t == null) {
            onCreated(scene);
        } else {
            onCreated(scene, t);
        }
        if (immDisplay()) {
            this.show();
        }
    }

    /**
     * This method is called when the fxml load is complete
     *
     * @param scene Javafx stage scene
     */
    protected void onCreated(final Scene scene) {

    }

    /**
     * This method is called when the fxml load is complete
     *
     * @param scene Javafx stage scene
     * @param t     External pass data
     */
    protected void onCreated(final Scene scene, final T t) {

    }

    /**
     * This method is used to receive an external incoming message.
     * It is mainly realized through {@link Vertx#eventBus()}</p>
     *
     * <note>
     * This method running in javafx thread,
     * so that you not need to call {@link Platform#runLater(Runnable)} method.</p>
     * </note>
     *
     * @param message External message
     */
    protected void notifyCall(final Message<Object> message) {

    }


    /**
     * When view show call that
     */
    protected void onShow(final Boolean show) {

    }

    /**
     * This method is called when the window is closed
     *
     * @param event Event source
     */
    protected void onClose(WindowEvent event) {
        this.showingProperty().removeListener(showListener);
        this.iconifiedProperty().removeListener(iconifyListener);
        VertxUtils.unRegisterEventBus(this.getClass().getName());
    }

    protected void onIconify(final Boolean icon) {

    }



    /**
     * When the {@link this#onCreated(Scene)} or {@link this#onCreated(Scene, Object)} method is executed,
     * this method is called to determine whether the window needs to be displayed immediately.
     * By default, it is displayed immediately.If you do not want to display it immediately,
     * you can override the implementation of this method</p>
     *
     * @return is immediately show view
     */
    protected boolean immDisplay() {
        return true;
    }
}

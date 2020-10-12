package cn.navigational.dbfx;

import cn.navigational.dbfx.kit.i18n.I18N;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * FXML helper
 *
 * @author yangkui
 * @since 1.0
 */
public class FXMLHelper {

    private static final Logger LOG = LoggerFactory.getLogger(FXMLHelper.class);

    /**
     * Load fxml view
     *
     * @param path FXML view path
     */
    public static <P> P loadFxml(final String path, Object controller) {
        var fxLoader = new FXMLLoader();
        fxLoader.setResources(I18N.getBundle());
        fxLoader.setController(controller);
        final P root;
        try {
            root = fxLoader.load(ClassLoader.getSystemResourceAsStream(path));
        } catch (IOException e) {
            LOG.debug("Load fxml view happen error.", e);
            throw new RuntimeException("Load fxml view failed path=[" + path + "].");
        }
        return root;
    }
}

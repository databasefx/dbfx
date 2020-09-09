package cn.navigational.dbfx;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * FXML helper
 *
 * @author yangkui
 * @since 1.0
 */
public class FXMLHelper {
    /**
     * Load fxml view
     *
     * @param path FXML view path
     */
    public static <P> P loadFxml(final String path, Object controller) {
        var fxLoader = new FXMLLoader();
        fxLoader.setController(controller);
        final P root;
        try {
            root = fxLoader.load(ClassLoader.getSystemResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException("Load fxml view failed path=[" + path + "].");
        }
        return root;
    }
}

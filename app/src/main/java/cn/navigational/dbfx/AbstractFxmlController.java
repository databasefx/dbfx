package cn.navigational.dbfx;


/**
 * FXML controller base class
 *
 * @param <P> Current fxml root node
 * @author yangkui
 * @since 1.0
 */
public class AbstractFxmlController<P> {
    private final P root;

    public AbstractFxmlController(String path) {
        this.root = FXMLHelper.loadFxml(path, this);
    }

    public P getParent() {
        return root;
    }

    /**
     * This method is called to release resources when the current view dies
     */
    public void dispose() {

    }
}

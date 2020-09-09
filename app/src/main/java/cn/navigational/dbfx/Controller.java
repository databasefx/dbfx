package cn.navigational.dbfx;


/**
 * FXML controller base class
 *
 * @param <T>
 * @author yangkui
 * @since 1.0
 */
public class Controller<T, P> {
    private final P root;

    public Controller(String path) {
        this(path, null);
    }

    public Controller(String path, T t) {
        root = FXMLHelper.<P>loadFxml(path, this);
        if (t == null) {
            this.onCreated(root);
        } else {
            this.onCreated(root, t);
        }
    }

    protected void onCreated(P root) {

    }

    protected void onCreated(P root, T t) {

    }

    public P getParent() {
        return root;
    }
}

package cn.navigational.dbfx;

import cn.navigational.dbfx.model.SQLClient;
import cn.navigational.dbfx.navigator.DatabaseItem;
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * Main view left navigator tree item
 *
 * @author yangkui
 * @since 1.0
 */
public class BaseTreeItem<T> extends TreeItem<T> {
    /**
     * Current TreeItem support MenuItem List
     */
    private final List<MenuItem> menuItems = new ArrayList<>();

    public BaseTreeItem() {
    }

    public BaseTreeItem(String icon) {
        this.setIcon(icon);
    }

    /**
     * Show a database icon
     *
     * @param icon icon path
     */
    protected void setIcon(String icon) {
        var image = SvgImageTranscoder.svgToImage(icon);
        this.setGraphic(new ImageView(image));
    }

    /**
     * Obtain support MenuItem List
     *
     * @return {@link MenuItem} List
     */
    public List<MenuItem> getSupportMenu() {
        return menuItems;
    }

    /**
     * Current TreeItem whether loading.
     * <note>
     * This state can be used when some a needs to display the load status.
     * </note>
     */
    private final BooleanProperty loadStatus = new SimpleBooleanProperty();

    public boolean isLoadStatus() {
        return loadStatus.get();
    }

    public BooleanProperty loadStatusProperty() {
        return loadStatus;
    }

    public void setLoadStatus(boolean loadStatus) {
        this.loadStatus.set(loadStatus);
    }

    /**
     * That method use quickly find a {@link DatabaseItem} for {@link SQLClient} object
     *
     * @return {@link SQLClient}
     */
    protected SQLClient getCurrentClient() {
        var parent = (BaseTreeItem<T>) this;
        while (parent != null) {
            if (parent instanceof DatabaseItem) {
                return ((DatabaseItem) parent).getSQLClient();
            }
            parent = (BaseTreeItem<T>) parent.getParent();
        }
        throw new RuntimeException("Not found any DatabaseTreeItem node!");
    }
}

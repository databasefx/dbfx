package cn.navigational.dbfx;

import cn.navigational.dbfx.kit.utils.StringUtils;
import cn.navigational.dbfx.model.SQLClient;
import cn.navigational.dbfx.navigator.DatabaseItem;
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    protected final Logger logger;

    public BaseTreeItem() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public BaseTreeItem(String icon) {
        this();
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
    private final BooleanProperty loadStatus = new SimpleBooleanProperty(false, "loadStatus");

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
        while (true) {
            if (parent instanceof DatabaseItem) {
                return ((DatabaseItem) parent).getSQLClient();
            }
            var temp = parent.getParent();
            if (temp == null || temp.getParent() == null) {
                break;
            }
            parent = (BaseTreeItem<T>) temp;
        }
        logger.error("Not found any DatabaseTreeItem node,current node {}", this);
        throw new RuntimeException("Not found any DatabaseTreeItem node!");
    }

    /**
     * Gets the full path of the current {@link TreeItem}
     *
     * @return Full path
     */
    protected String getFullPath() {
        var tabPath = new StringBuilder();
        var parent = (BaseTreeItem<T>) this;
        while (true) {
            var subPath = parent.getValue().toString();
            if (parent instanceof DatabaseItem) {
                var client = ((DatabaseItem) parent).getSQLClient();
                subPath = client.getDbInfo().getUuid();
            }
            tabPath.insert(0, "\\" + subPath);
            var temp = parent.getParent();
            //If current item is root item,stop loop
            if (temp == null || temp.getParent() == null) {
                tabPath = new StringBuilder(tabPath.substring(1));
                break;
            }
            parent = (BaseTreeItem<T>) temp;
        }
        if (StringUtils.isEmpty(tabPath.toString())) {
            logger.error("Not found every path for [{}]", this);
            throw new RuntimeException("Not found every path for [" + this + "]");
        }
        return tabPath.toString();
    }
}

package cn.navigational.dbfx.controls.tree.impl;

import cn.navigational.dbfx.controls.tree.AbstractBaseTreeItem;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;


/**
 * Progress TreeItem
 *
 * @author yangkui
 * @since 1.0
 */
public abstract class ProgressTreeItem extends AbstractBaseTreeItem {
    /**
     * Load status
     */
    protected final BooleanProperty loadStatus;
    /**
     * Load status show suffix graphic
     */
    private final ProgressIndicator indicator;
    /**
     * Load status change listener
     */
    private final ChangeListener<Boolean> statusListener;
    /**
     * Prefix graphic
     */
    private Node prefixGra = null;


    public ProgressTreeItem() {
        this(null);
    }

    public ProgressTreeItem(Node prefixGra) {
        this.setPrefixGra(prefixGra);
        this.statusListener = statusChange();
        this.indicator = new ProgressIndicator();
        this.loadStatus = new SimpleBooleanProperty(null, "loadStatus", false);
        this.loadStatus.addListener(this.statusListener);
    }

    public ProgressTreeItem(Node prefixGra, Node suffixGra) {
        this(prefixGra);
        this.setSuffixGra(suffixGra);
    }

    /**
     * Load status change callback that method
     *
     * @return {@link ChangeListener<Boolean>}
     */
    private ChangeListener<Boolean> statusChange() {
        return ((observable, oldValue, newValue) -> {
            final Node node;
            if (newValue) {
                prefixGra = getPrefixGra();
                node = indicator;
            } else {
                node = prefixGra;
            }
            Platform.runLater(() -> setPrefixGra(node));
        });
    }

    @Override
    public void dispose() {
        super.dispose();
        this.loadStatus.removeListener(this.statusListener);
    }
}

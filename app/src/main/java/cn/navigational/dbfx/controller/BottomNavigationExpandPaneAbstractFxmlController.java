package cn.navigational.dbfx.controller;

import cn.navigational.dbfx.AbstractFxmlController;
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import java.util.List;

import static cn.navigational.dbfx.config.AppConstantsKt.*;
import static cn.navigational.dbfx.config.ControllerPathKt.B_N_EXPAND_PANE_PANE;

/**
 * Home view bottom navigation expand pane
 *
 * @author yangkui
 * @since 1.0
 */
public class BottomNavigationExpandPaneAbstractFxmlController extends AbstractFxmlController<BorderPane> {
    @FXML
    private Button mini;
    @FXML
    private Label paneTitle;
    @FXML
    private Button setting;

    public BottomNavigationExpandPaneAbstractFxmlController() {
        super(B_N_EXPAND_PANE_PANE);
        mini.setGraphic(SvgImageTranscoder.svgToImageView(W_MINI));
        setting.setGraphic(SvgImageTranscoder.svgToImageView(SETTING));
        mini.setOnAction(event -> {
            var pane = paneProperty.get();
            if (pane == null) {
                return;
            }
            //call close method
            pane.close();
            this.paneProperty.set(null);
        });
        paneProperty.addListener(((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            this.paneTitle.setText(newValue.getTitle());
            getParent().setCenter((Node) newValue.getParent());
        }));
    }

    private final ObjectProperty<ExpandPaneProvider> paneProperty = new SimpleObjectProperty<>(null, "paneProperty", null);

    public ExpandPaneProvider getPaneProperty() {
        return paneProperty.get();
    }

    public ObjectProperty<ExpandPaneProvider> panePropertyProperty() {
        return paneProperty;
    }

    public void setPaneProperty(ExpandPaneProvider provider) {
        var pane = getPaneProperty();
        if (pane != null && pane.getClass() == provider.getClass()) {
            this.paneProperty.set(null);
            return;
        }
        this.paneProperty.set(provider);
    }

    public static abstract class ExpandPaneProvider<P> extends AbstractFxmlController<P> {
        public ExpandPaneProvider(String path) {
            super(path);
        }

        /**
         * Get current Expand pane setting
         *
         * @return {@link MenuItem} list
         */
        abstract List<MenuItem> getSetting();

        /**
         * Get current expand pane title
         *
         * @return Expand pane title
         */
        abstract String getTitle();

        /**
         * Current expand pane close call that method
         */
        abstract void close();
    }
}

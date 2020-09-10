package cn.navigational.dbfx.dialogs;

import cn.navigational.dbfx.tool.svg.SvgImageTranscoder;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import static cn.navigational.dbfx.config.AppConstantsKt.APP_STYLE;
import static cn.navigational.dbfx.config.AppConstantsKt.INFO_ICON;

/**
 * Show a confirm dialog
 *
 * @author yangkui
 * @since 1.0
 */
public class SimpleConfirmDialog extends Alert {
    /**
     * Info icon
     */
    private static final Image INFO_IMAGE = SvgImageTranscoder.svgToImage(INFO_ICON);
    /**
     * Default css class name
     */
    private static final String DEFAULT_STYLE_CSS = "confirm-alert";

    public SimpleConfirmDialog(String content) {
        super(AlertType.CONFIRMATION);

        this.setGraphic(null);
        this.setHeaderText(null);
        this.setContentText(null);

        var hBox = new HBox();
        var label = new Label(content);
        var icon = new ImageView(INFO_IMAGE);

        getDialogPane().setContent(hBox);
        hBox.getChildren().addAll(icon, label);
        getDialogPane().getStylesheets().add(APP_STYLE);
        getDialogPane().getStyleClass().add(DEFAULT_STYLE_CSS);
    }
}

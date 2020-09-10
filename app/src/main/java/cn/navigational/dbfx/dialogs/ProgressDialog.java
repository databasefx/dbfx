package cn.navigational.dbfx.dialogs;

import javafx.beans.property.DoubleProperty;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import static cn.navigational.dbfx.config.AppConstantsKt.APP_STYLE;

/**
 * A progress dialog
 *
 * @author yangkui
 * @since 1.0
 */
public class ProgressDialog extends Dialog<Double> {

    private final VBox vBox = new VBox();
    private final Label tipText = new Label();
    private final ProgressBar bar = new ProgressBar();

    private final static String DEFAULT_STYLE_CLASS = "progress-dialog";

    public ProgressDialog() {
        initUi();
        initStyle(StageStyle.UNDECORATED);
        getDialogPane().getStylesheets().add(APP_STYLE);
        getDialogPane().getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    private void initUi() {
        var pane = new DialogPane();
        var lBox = new HBox();
        var rBox = new HBox();
        lBox.getChildren().add(bar);
        rBox.getChildren().add(tipText);
        bar.prefWidthProperty().bind(lBox.widthProperty());
        vBox.getChildren().addAll(lBox, rBox);
        pane.setGraphic(vBox);
        setDialogPane(pane);
    }

    public DoubleProperty getProgressProperty() {
        return bar.progressProperty();
    }

    public void setTipText(String tip) {
        this.tipText.setText(tip);
    }
}

package cn.navigational.dbfx.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.controlsfx.dialog.ExceptionDialog;

import static cn.navigational.dbfx.config.AppConstantsKt.APP_STYLE;


/**
 * Show a alert dialog
 *
 * @author yangkui
 * @since 1.0
 */
public class AlertUtils {

    public static void showSimpleDialog(String msg) {
        showSimpleDialog("消息", msg);
    }

    /**
     * Show a simple alert dialog.
     *
     * @param title alert title
     * @param msg   alert message
     */
    public static void showSimpleDialog(String title, String msg) {
        Platform.runLater(() -> {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText("");
            alert.setContentText(msg);
            alert.setResizable(false);
            alert.setResizable(true);
            var dialog = alert.getDialogPane();
            //remove icon
            dialog.setGraphic(null);
            dialog.getStylesheets().add(APP_STYLE);
            alert.show();
        });
    }

    /**
     * Show a exception dialog
     *
     * @param title Error title
     * @param e     Error stack trace
     */
    public static void showExDialog(String title, Throwable e) {
        Platform.runLater(() -> {
            var ex = new ExceptionDialog(e);
            ex.setTitle("Error");
            ex.setHeaderText(title);
            ex.getDialogPane().getStylesheets().add(APP_STYLE);
            ex.show();
        });
    }
}

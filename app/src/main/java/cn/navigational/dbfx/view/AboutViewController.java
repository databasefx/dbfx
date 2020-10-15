package cn.navigational.dbfx.view;

import cn.navigational.dbfx.ViewController;

import cn.navigational.dbfx.i18n.I18N;
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder;
import cn.navigational.dbfx.utils.AppSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import static cn.navigational.dbfx.config.AppConstantsKt.APP_ICON;
import static cn.navigational.dbfx.config.AppConstantsKt.DIALOG_CLOSE_ICON;
import static cn.navigational.dbfx.config.ViewPathKt.ABOUT_PAGE;

/**
 * Application about view
 *
 * @author yangkui
 * @since 1.0
 */
public class AboutViewController extends ViewController<BorderPane> {
    @FXML
    private Label vm;
    @FXML
    private Label bTime;
    @FXML
    private Button close;
    @FXML
    private Label version;
    @FXML
    private Label runtime;
    @FXML
    private ImageView icon;
    @FXML
    private Label copyright;

    private static final RuntimeMXBean RUNTIME = ManagementFactory.getRuntimeMXBean();


    public AboutViewController() {
        super(ABOUT_PAGE);
        this.getStage().setAlwaysOnTop(true);
        this.getStage().initStyle(StageStyle.UNDECORATED);
        this.getStage().initModality(Modality.APPLICATION_MODAL);
        this.close.setOnAction(event -> this.getStage().close());
        this.runtime.setText(I18N.getString("about.options.runtime.version", RUNTIME.getVmVersion()));
        this.vm.setText("VM: " + RUNTIME.getVmName() + " by " + RUNTIME.getVmVendor());
        this.icon.setImage(SvgImageTranscoder.svgToImage(APP_ICON));
        this.close.setGraphic(SvgImageTranscoder.svgToImageView(DIALOG_CLOSE_ICON));
    }

    /**
     * Load MANIFEST.MF file content render some applicarion info
     */
    private void initLocalInfo() {
        var manifest = AppSettings.getAppSettings().getManifest();
        this.version.setText("V " + manifest.getVersion());
        this.copyright.setText(manifest.getCopyright());
        this.bTime.setText(I18N.getString("about.options.build.time", manifest.getBuildTime()));
    }
}

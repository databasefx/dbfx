package cn.navigational.dbfx.view;

import cn.navigational.dbfx.View;

import cn.navigational.dbfx.kit.i18n.I18N;
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import static cn.navigational.dbfx.config.AppConstantsKt.APP_ICON;
import static cn.navigational.dbfx.config.AppConstantsKt.DIALOG_CLOSE_ICON;
import static cn.navigational.dbfx.config.ViewPathKt.ABOUT_PAGE;
import static cn.navigational.dbfx.io.DbFxIOKt.loadManifest;

/**
 * Application about view
 *
 * @author yangkui
 * @since 1.0
 */
public class AboutView extends View<Void> {
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


    public AboutView() {
        super(ABOUT_PAGE);
    }

    @Override
    protected void onCreated(Scene scene) {
        this.initLocalInfo();
        this.setAlwaysOnTop(true);
        this.initStyle(StageStyle.UNDECORATED);
        this.initModality(Modality.APPLICATION_MODAL);
        this.close.setOnAction(event -> this.close());
        this.runtime.setText(I18N.getString("about.options.runtime.version", RUNTIME.getVmVersion()));
        this.vm.setText("VM: " + RUNTIME.getVmName() + " by " + RUNTIME.getVmVendor());
        this.icon.setImage(SvgImageTranscoder.svgToImage(APP_ICON));
        this.close.setGraphic(SvgImageTranscoder.svgToImageView(DIALOG_CLOSE_ICON));
    }

    /**
     * Load MANIFEST.MF file content render some applicarion info
     */
    private void initLocalInfo() {
        var manifest = loadManifest();
        var version = manifest.get("App-Version");
        var copyright = manifest.get("Copyright");
        var buildTime = manifest.get("Build-Time");
        this.version.setText("V " + version);
        this.copyright.setText(copyright);
        this.bTime.setText(I18N.getString("about.options.build.time", buildTime));
    }
}

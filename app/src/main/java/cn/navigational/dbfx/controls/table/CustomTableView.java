package cn.navigational.dbfx.controls.table;

import cn.navigational.dbfx.model.TableSetting;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;

/**
 * Custom Table view
 *
 * @author yangkui
 * @since 1.0
 */
public class CustomTableView extends TableView<ObservableList<StringProperty>> {
    /**
     * Default load show placeholder
     */
    private final ProgressIndicator loadBar = new ProgressIndicator();
    /**
     * Table setting property
     */
    private final ObjectProperty<TableSetting> tableSetting = new SimpleObjectProperty<>(null, "tableSetting", null);
    /**
     * Current table load status
     */
    private final BooleanProperty loadStatus = new SimpleBooleanProperty(null, "loadingProperty", false);
    /**
     * Default style class
     */
    private static final String DEFAULT_STYLE_CLASS = "custom_table_view";

    private final ChangeListener<Boolean> listener = ((observable, oldValue, newValue) -> {
        final Node node;
        if (newValue) {
            node = loadBar;
            this.getItems().clear();
        } else {
            node = null;
        }
        Platform.runLater(() -> this.setPlaceholder(node));
    });

    public CustomTableView() {
        this(new TableSetting());
    }

    public CustomTableView(TableSetting setting) {
        this.setEditable(true);
        this.setTableSetting(setting);
        //disable column sort
        this.setSortPolicy(e -> null);
        this.loadStatus.addListener(this.listener);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.getSelectionModel().setCellSelectionEnabled(true);
        this.getColumns().add(new CustomTableColumn(true));
    }

    public boolean isLoadStatus() {
        return loadStatus.get();
    }

    public BooleanProperty loadStatusProperty() {
        return loadStatus;
    }

    public void setLoadStatus(boolean loadStatus) {
        this.loadStatus.set(loadStatus);
    }

    public TableSetting getTableSetting() {
        return tableSetting.get();
    }

    public ObjectProperty<TableSetting> tableSettingProperty() {
        return tableSetting;
    }

    public void setTableSetting(TableSetting tableSetting) {
        this.tableSetting.set(tableSetting);
    }

    public void dispose() {
        this.loadStatus.removeListener(this.listener);
    }
}

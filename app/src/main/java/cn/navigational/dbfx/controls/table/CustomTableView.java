package cn.navigational.dbfx.controls.table;

import cn.navigational.dbfx.kit.model.TableColumnMeta;
import cn.navigational.dbfx.kit.utils.OssUtils;
import cn.navigational.dbfx.model.TableSetting;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.List;
import java.util.stream.Collectors;

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

    private final TableViewContextMenu contextMenu = new TableViewContextMenu();

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


    private final ChangeListener<Boolean> cellSelectListener = ((observable, oldValue, newValue) -> this.contextMenu.updateMenuItems(newValue));

    public CustomTableView() {
        this(new TableSetting());
    }

    public CustomTableView(TableSetting setting) {
        this.setEditable(true);
        this.setTableSetting(setting);
        //disable column sort
        this.setSortPolicy(e -> null);
        this.setContextMenu(this.contextMenu);
        this.loadStatus.addListener(this.listener);
        this.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.getSelectionModel().setCellSelectionEnabled(true);
        this.getColumns().add(new CustomTableColumn(true));
        this.getSelectionModel().cellSelectionEnabledProperty().addListener(this.cellSelectListener);
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
        this.getSelectionModel().cellSelectionEnabledProperty().removeListener(this.cellSelectListener);
    }

    private class TableViewContextMenu extends ContextMenu {


        private final Menu copyItem = new Menu("Copy");
        private final MenuItem copyValItem = new MenuItem("Copy");

        public TableViewContextMenu() {
            var jsonItem = new MenuItem("JSON");
            var phpItem = new MenuItem("PHP Array");
            jsonItem.setOnAction(event -> copy(CopyType.JSON));
            phpItem.setOnAction(event -> copy(CopyType.PHP_ARRAY));
            this.copyValItem.setOnAction(event -> copy(CopyType.VALUE));
            this.copyItem.getItems().addAll(jsonItem, phpItem);
            this.getItems().addAll(copyItem);
        }

        private void updateMenuItems(boolean model) {
            var items = getItems();
            if (model) {
                items.remove(copyItem);
                if (!items.contains(copyValItem)) {
                    items.add(copyValItem);
                }
            } else {
                items.remove(copyValItem);
                if (!items.contains(copyItem)) {
                    items.add(copyItem);
                }
            }
        }
    }

    /**
     * The corresponding string is generated according to the specified
     * format and added to the system clipboard</p>
     *
     * @param type Target type
     */
    private void copy(CopyType type) {
        var selectModel = this.getSelectionModel();
        var index = selectModel.getSelectedIndex();
        if (index == -1 && type != CopyType.VALUE) {
            return;
        }
        if (type == CopyType.JSON || type == CopyType.PHP_ARRAY) {
            var item = selectModel.getSelectedItem();
            var colNames = getColumns().stream().map(TableColumn::getText).collect(Collectors.toList());
            var json = new JsonObject();
            for (int i = 1; i < colNames.size() - 1; i++) {
                json.put(colNames.get(i), item.get(i).getValue());
            }
            final String str;
            if (type == CopyType.JSON) {
                str = json.toString();
            } else {
                str = new JsonArray().add(json).toString();
            }
            OssUtils.addStrToClipboard(str);
        }
        if (type == CopyType.VALUE) {
            var cells = selectModel.getSelectedCells();
            if (cells.isEmpty()) {
                return;
            }
            var pos = cells.get(0);
            var row = pos.getRow();
            var column = pos.getColumn();
            var str = getItems().get(row).get(column - 1).get();
            OssUtils.addStrToClipboard(str);
        }
    }

    /**
     * Copy value type enum
     */
    private enum CopyType {
        /**
         * JSON string
         */
        JSON,
        /**
         * Plain string
         */
        VALUE,
        /**
         * PHP array str
         */
        PHP_ARRAY
    }


}

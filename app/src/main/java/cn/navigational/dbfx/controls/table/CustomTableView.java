package cn.navigational.dbfx.controls.table;

import cn.navigational.dbfx.i18n.I18N;
import cn.navigational.dbfx.kit.utils.OssUtils;
import cn.navigational.dbfx.model.TableSetting;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.util.stream.Collectors;

/**
 * Custom Table view
 *
 * @author yangkui
 * @since 1.0
 */
public class CustomTableView extends TableView<ObservableList<StringProperty>> {
    /**
     * Current table view support menu list
     */
    public static enum CustomTableViewAction {
        /**
         * Edit data
         */
        EDIT,
        /**
         * Flush data
         */
        FLUSH,
        /**
         * New row
         */
        ADD_ROW,
        /**
         * Delete row
         */
        DELETE_ROW,
    }

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
    }

    public void registerMenu(CustomTableViewAction action, EventHandler<ActionEvent> eventHandler) {
        this.contextMenu.updateMenuItems(action, eventHandler);
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

    private class TableViewContextMenu extends ContextMenu {

        private final MenuItem editItem = new MenuItem(I18N.getString("label.edit"));
        private final MenuItem flushItem = new MenuItem(I18N.getString("label.flush"));
        private final MenuItem addRowItem = new MenuItem(I18N.getString("label.add.new.row"));
        private final MenuItem delRowItem = new MenuItem(I18N.getString("label.del.rows"));

        public TableViewContextMenu() {
            var copyItem = new Menu(I18N.getString("label.copy"));
            var jsonItem = new MenuItem(I18N.getString("label.json"));
            var valueItem = new MenuItem(I18N.getString("label.string"));
            var phpItem = new MenuItem(I18N.getString("label.php.array"));
            jsonItem.setOnAction(event -> copy(CopyType.JSON));
            phpItem.setOnAction(event -> copy(CopyType.PHP_ARRAY));
            valueItem.setOnAction(event -> copy(CopyType.VALUE));
            copyItem.getItems().addAll(valueItem, jsonItem, phpItem);
            this.getItems().add(copyItem);
        }

        private void updateMenuItems(CustomTableViewAction action, EventHandler<ActionEvent> handler) {
            //If edit external not pass handler wo will use default edit strategy
            if (action == CustomTableViewAction.EDIT) {
                editItem.setOnAction(handler == null ? edit() : handler);
                this.getItems().add(editItem);
            } else {
                assert handler != null;
            }
            if (action == CustomTableViewAction.ADD_ROW) {
                addRowItem.setOnAction(handler);
                this.getItems().add(addRowItem);
            }
            if (action == CustomTableViewAction.DELETE_ROW) {
                delRowItem.setOnAction(handler);
                this.getItems().add(delRowItem);
            }
            if (action == CustomTableViewAction.FLUSH) {
                flushItem.setOnAction(handler);
                this.getItems().add(flushItem);
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
        var selectCell = selectModel.cellSelectionEnabledProperty().get();
        var json = new JsonObject();
        if (selectCell) {
            var sCell = selectModel.getSelectedCells();
            if (sCell.isEmpty()) {
                return;
            }
            var pos = sCell.get(0);
            var row = pos.getRow();
            var col = pos.getColumn();
            var item = getItems().get(row);
            var val = item.get(col - 1);
            json.put(pos.getTableColumn().getText(), val.getValue());
        } else {
            var item = selectModel.getSelectedItem();
            var colNames = getColumns().stream().map(TableColumn::getText).collect(Collectors.toList());
            for (int i = 1; i < colNames.size() - 1; i++) {
                json.put(colNames.get(i), item.get(i - 1).getValue());
            }
        }
        final String str;
        if (type == CopyType.JSON) {
            str = json.toString();
        } else if (type == CopyType.PHP_ARRAY) {
            str = new JsonArray().add(json).toString();
        } else {
            var list = json.getMap().values().stream().map(Object::toString).collect(Collectors.toList());
            str = String.join("|", list);
        }
        OssUtils.addStrToClipboard(str);
    }

    private EventHandler<ActionEvent> edit() {

        return event -> {

        };
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

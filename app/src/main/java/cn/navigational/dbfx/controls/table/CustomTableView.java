package cn.navigational.dbfx.controls.table;

import cn.navigational.dbfx.model.TableSetting;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 * Custom Table view
 *
 * @author yangkui
 * @since 1.0
 */
public class CustomTableView extends TableView<ObservableList<StringProperty>> {
    /**
     * Table setting property
     */
    private final ObjectProperty<TableSetting> tableSetting = new SimpleObjectProperty<>(null, "tableSetting", null);

    public CustomTableView() {
        this(new TableSetting());
    }

    public CustomTableView(TableSetting setting) {
        this.setEditable(true);
        this.setTableSetting(setting);
        this.getSelectionModel().setCellSelectionEnabled(true);
        this.getColumns().add(new CustomTableColumn(true));
        //disable column sort
        this.setSortPolicy(e -> null);
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
}

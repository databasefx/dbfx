package cn.navigational.dbfx.convert;

import cn.navigational.dbfx.kit.config.ConstantsKt;
import cn.navigational.dbfx.model.TableSetting;
import cn.navigational.dbfx.utils.DateUtils;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * {@link io.vertx.sqlclient.RowSet<io.vertx.sqlclient.Row>} convert program
 *
 * @author yangkui
 * @since 1.0
 */
public class RowSetConvert {
    /**
     * Batch convert row to JavaFX collection
     *
     * @param rowSet  Multi row data set
     * @param setting Current table setting
     * @return {@link List<ObservableList<StringProperty>}
     */
    public static List<ObservableList<StringProperty>> rowSetConvert(RowSet<Row> rowSet, TableSetting setting) {
        var list = new ArrayList<ObservableList<StringProperty>>();
        for (Row row : rowSet) {
            var item = rowConvert(row, setting);
            list.add(item);
        }
        return list;
    }

    /**
     * Convert single row data to JavaFX collection
     *
     * @param row     Single row data
     * @param setting Custom table settings
     * @return {@link ObservableList<StringProperty>}
     */
    public static ObservableList<StringProperty> rowConvert(Row row, TableSetting setting) {
        var item = FXCollections.<StringProperty>observableArrayList();
        //index column
        for (int i = 0; i < row.size(); i++) {
            var value = row.getValue(i);
            if (value == null) {
                value = ConstantsKt.NULL_TAG;
            }
            //format data time
            if (value instanceof LocalDateTime) {
                value = DateUtils.formatLocalTime((LocalDateTime) value, setting.getDtFormat());
            }
            item.add(new SimpleStringProperty(value.toString()));
        }
        return item;
    }

    public static void rowSetConvert(TableView<ObservableList<StringProperty>> tableView, RowSet<Row> rowSet, TableSetting setting) {
        var items = rowSetConvert(rowSet, setting);
        Platform.runLater(() -> {
            if (!tableView.getItems().isEmpty()) {
                tableView.getItems().clear();
            }
            tableView.getItems().addAll(items);
        });
    }
}

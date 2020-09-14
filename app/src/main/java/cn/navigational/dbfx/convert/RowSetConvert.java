package cn.navigational.dbfx.convert;

import cn.navigational.dbfx.kit.config.ConstantsKt;
import cn.navigational.dbfx.model.TableSetting;
import cn.navigational.dbfx.utils.DateUtils;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
     * @param offset  Current row in JavaFx collection position
     * @return {@link List<ObservableList<StringProperty>}
     */
    public static List<ObservableList<StringProperty>> rowSetConvert(RowSet<Row> rowSet, TableSetting setting, int offset) {
        var list = new ArrayList<ObservableList<StringProperty>>();
        for (Row row : rowSet) {
            var item = rowConvert(row, setting, offset);
            list.add(item);
            ++offset;
        }
        return list;
    }

    /**
     * Convert single row data to JavaFX collection
     *
     * @param row     Single row data
     * @param setting Custom table settings
     * @param offset  Current row in JavaFx collection position
     * @return {@link ObservableList<StringProperty>}
     */
    public static ObservableList<StringProperty> rowConvert(Row row, TableSetting setting, int offset) {
        var item = FXCollections.<StringProperty>observableArrayList();
        //index column
        item.add(new SimpleStringProperty(String.valueOf(offset)));
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
}

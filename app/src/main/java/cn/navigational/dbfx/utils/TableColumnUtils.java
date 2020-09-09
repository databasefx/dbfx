package cn.navigational.dbfx.utils;

import cn.navigational.dbfx.controls.table.CustomTableColumn;
import cn.navigational.dbfx.kit.model.TableColumnMeta;
import javafx.beans.property.SimpleStringProperty;

import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.List;

/**
 * TableColumn utils
 *
 * @author yangkui
 * @since 1.0
 */
public class TableColumnUtils {
    /**
     * Create  table data column
     *
     * @param metas database table column meta data
     * @return return {@link CustomTableColumn} list
     */
    public static List<CustomTableColumn> createTableDataColumn(List<TableColumnMeta> metas) {
        var columns = new ArrayList<CustomTableColumn>();
        //Add index column
        columns.add(createTableDataColumn(null, true));
        for (TableColumnMeta meta : metas) {
            var column = createTableDataColumn(meta, false);
            columns.add(column);
        }
        return columns;
    }

    /**
     * Create single {@link CustomTableColumn} instance object
     *
     * @param meta    Table meta data
     * @param isIndex That column is index column?
     * @return {@link CustomTableColumn}
     */
    public static CustomTableColumn createTableDataColumn(TableColumnMeta meta, boolean isIndex) {
        final CustomTableColumn column;
        if (isIndex) {
            column = new CustomTableColumn(true);
        } else {
            column = new CustomTableColumn(meta);
        }
        column.setCellValueFactory(cellDataFeatures -> {
            var tableView = cellDataFeatures.getTableView();
            var tableColumn = cellDataFeatures.getTableColumn();
            var columnIndex = tableView.getColumns().indexOf(tableColumn);
            var values = cellDataFeatures.getValue();
            if (columnIndex >= values.size()) {
                return new SimpleStringProperty("");
            } else {
                return cellDataFeatures.getValue().get(columnIndex);
            }
        });
        return column;
    }
}
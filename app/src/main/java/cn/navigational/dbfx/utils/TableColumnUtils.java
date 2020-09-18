package cn.navigational.dbfx.utils;

import cn.navigational.dbfx.controls.table.CustomTableColumn;
import cn.navigational.dbfx.kit.model.TableColumnMeta;

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
        for (TableColumnMeta meta : metas) {
            var column = createTableDataColumn(meta);
            columns.add(column);
        }
        return columns;
    }

    /**
     * Create single {@link CustomTableColumn} instance object
     *
     * @param meta Table meta data
     * @return {@link CustomTableColumn}
     */
    public static CustomTableColumn createTableDataColumn(TableColumnMeta meta) {
        var column = new CustomTableColumn(meta);
        column.setCellValueFactory(cellDataFeatures -> {
            var tableView = cellDataFeatures.getTableView();
            var tableColumn = cellDataFeatures.getTableColumn();
            var columnIndex = tableView.getColumns().indexOf(tableColumn);
            return cellDataFeatures.getValue().get(columnIndex);
        });
        return column;
    }
}
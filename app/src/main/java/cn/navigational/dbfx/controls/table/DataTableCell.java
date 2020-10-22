package cn.navigational.dbfx.controls.table;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;

import static cn.navigational.dbfx.kit.config.ConstantsKt.NULL_TAG;

/**
 * Table data cell
 *
 * @author yangkui
 * @since 1.0
 */
public class DataTableCell extends TableCell<ObservableList<StringProperty>, String> {
    public static final String NULL_STYLE = "null-style";

    DataTableCell() {
        setWrapText(false);
        addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (isEmpty() || getTableColumn() == null || getTableRow() == null) {
                return;
            }
            var selectModel = getTableView().getSelectionModel();
            selectModel.setCellSelectionEnabled(true);
            var rowIndex = getTableRow().getIndex();
            selectModel.select(rowIndex, getTableColumn());
        });
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        var status = (empty || getTableColumn() == null || getTableRow() == null);
        if (status) {
            setText(null);
            return;
        }
        var value = item;
        var table = (CustomTableView) getTableView();
        var styleClass = getStyleClass();
        if (item.equals(NULL_TAG)) {
            value = table.getTableSetting().getNulValue();
            if (!styleClass.contains(NULL_STYLE)) {
                styleClass.add(NULL_STYLE);
            }
        } else {
            styleClass.remove(NULL_STYLE);
        }
        setText(value);
    }
}

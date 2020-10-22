package cn.navigational.dbfx.controls.table;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;

/**
 * Index table cell
 *
 * @author yangkui
 * @since 1.0
 */
public class IndexTableCell extends TableCell<ObservableList<StringProperty>, String> {
    public IndexTableCell() {
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (isEmpty() || getTableRow() == null) {
                return;
            }
            var selectionModel = getTableView().getSelectionModel();
            selectionModel.setCellSelectionEnabled(false);
            selectionModel.select(getTableRow().getIndex());
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow() == null) {
            setText(null);
            return;
        }
        setText(String.valueOf(getTableRow().getIndex() + 1));
    }
}

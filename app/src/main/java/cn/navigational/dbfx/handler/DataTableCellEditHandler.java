package cn.navigational.dbfx.handler;

import cn.navigational.dbfx.kit.enums.DataType;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * Data table cell editor handler
 *
 * @author yangkui
 * @since 1.0
 */
public class DataTableCellEditHandler {
    private final Node graphics;
    private final DataType dataType;

    private DataTableCellEditHandler(DataType dataType) {
        this.dataType = dataType;
        this.graphics = new TextField();
    }

    public Node getEditor() {
        return graphics;
    }

    public static DataTableCellEditHandler getEditorHandler(DataType type) {
        return new DataTableCellEditHandler(type);
    }
}

package cn.navigational.dbfx.controls.table;

import cn.navigational.dbfx.kit.model.TableColumnMeta;
import cn.navigational.dbfx.kit.utils.StringUtils;
import cn.navigational.dbfx.tool.svg.SvgImageTranscoder;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.util.Arrays;

import static cn.navigational.dbfx.config.AppConstantsKt.PRI_KEY_ICON;
import static cn.navigational.dbfx.config.AppConstantsKt.TABLE_FIELD_ICON;

/**
 * Custom table column
 *
 * @author yangkui
 * @since 1.0
 */
public class CustomTableColumn extends TableColumn<ObservableList<StringProperty>, String> {
    /**
     * Default column max width
     */
    private static final double MAX_COLUMN_WIDTH = 200.0;
    /**
     * Default column min width
     */
    private static final double MIN_COLUMN_WIDTH = 100.0;
    /**
     * Default index column width
     */
    private static final double INDEX_COLUMN_WIDTH = 40.0;
    /**
     * Index column default style class
     */
    private static final String INDEX_DEFAULT_STYLE_CLASS = "index-column";
    /**
     * Data column default style class
     */
    private static final String DATA_COLUMN_DEFAULT_STYLE_CLASS = "data-column";
    /**
     * PRI column image icon
     */
    private static final Image PRIMARY_ICON = SvgImageTranscoder.svgToImage(PRI_KEY_ICON);
    /**
     * Common column image icon
     */
    private static final Image FIELD_ICON = SvgImageTranscoder.svgToImage(TABLE_FIELD_ICON);

    /**
     * Get current column filed image
     *
     * @param meta Table column meta data
     * @return {@link Image}
     */
    public static Image getFieldImage(TableColumnMeta meta) {
        var pri = meta.getConstrainTypes() != null
                && Arrays.stream(meta.getConstrainTypes()).allMatch(it -> it == TableColumnMeta.ConstrainType.PRIMARY_KEY);
        final Image image;
        if (pri) {
            image = PRIMARY_ICON;
        } else {
            image = FIELD_ICON;
        }
        return image;
    }

    /**
     * Current column is index column?
     */
    private final boolean indexColumn;

    /**
     * When current column as data column use that constructor
     */
    private final ObjectProperty<TableColumnMeta> tableColumnMeta = new SimpleObjectProperty<>(null, "tableColumnMeta");

    public CustomTableColumn(TableColumnMeta column) {
        this(false);
        this.updateTableColumn(column);

    }

    public CustomTableColumn(boolean indexColumn) {
        this.indexColumn = indexColumn;
        if (indexColumn) {
            setSortable(false);
            this.setMaxWidth(INDEX_COLUMN_WIDTH);
            this.setCellFactory((e) -> new IndexTableCell());
            this.getStyleClass().add(INDEX_DEFAULT_STYLE_CLASS);
        } else {
            this.setCellFactory(e -> new DataTableCell());
            this.getStyleClass().add(DATA_COLUMN_DEFAULT_STYLE_CLASS);
        }
    }

    public void updateTableColumn(TableColumnMeta column) {
        var colName = column.getColName();
        var tip = colName;
        if (StringUtils.isNotEmpty(column.getType())) {
            tip = String.format("%s:%s", tip, column.getType());
        }
        if (column.getLength() != null) {
            tip = String.format("%s(%s)", tip, column.getLength());
        }
        this.tableColumnMeta.set(column);
        var label = new Label();
        label.setTooltip(new Tooltip(tip));
        label.setGraphic(new ImageView(getFieldImage(column)));
        var mWidth = calColumnWidth(column);
        Platform.runLater(() -> {
            this.setText(colName);
            this.setGraphic(label);
            this.setMinWidth(mWidth);
        });
    }

    /**
     * Calculate table column length
     *
     * @param column Table column meta data
     * @return Calculate length
     */
    private double calColumnWidth(TableColumnMeta column) {
        var title = column.getColName();
        var fontSize = Font.getDefault().getSize();
        var iWidth = getFieldImage(column).getWidth();
        var tWidth = title.length() * fontSize;
        var txtWidth = column.getLength() * fontSize;
        var temp = iWidth + tWidth;
        var mWidth = Math.max(txtWidth, temp);
        final double realLen;
        if (mWidth > MAX_COLUMN_WIDTH) {
            realLen = MAX_COLUMN_WIDTH;
        } else if (mWidth < MIN_COLUMN_WIDTH) {
            realLen = MIN_COLUMN_WIDTH;
        } else {
            realLen = mWidth;
        }
        return realLen;
    }

    /**
     * Get current column for TableColumnMeta
     */
    public TableColumnMeta getTableColumnMeta() {
        if (indexColumn) {
            throw new RuntimeException("Current column is index column,so not have TableColumnMeta!");
        }
        return tableColumnMeta.get();
    }
}

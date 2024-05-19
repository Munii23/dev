package at.fhtw.tourplanner.model;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableViewHelper {
    public static void stretchTableColumnsToFill(TableView<?> tableView, TableColumn<?, ?>... columns) {
        if (tableView == null || columns == null) {
            throw new IllegalArgumentException("TableView and columns cannot be null.");
        }

        double totalColumns = columns.length;
        for (TableColumn<?, ?> column : columns) {
            column.prefWidthProperty().bind(tableView.widthProperty().divide(totalColumns));
        }
    }
}

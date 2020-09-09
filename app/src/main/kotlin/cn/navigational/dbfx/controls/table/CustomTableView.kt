package cn.navigational.dbfx.controls.table


import javafx.beans.property.StringProperty
import javafx.collections.ObservableList
import javafx.scene.control.TableView

class CustomTableView : TableView<ObservableList<StringProperty>>() {

    init {
        //Default start select cell
        this.selectionModel.isCellSelectionEnabled = true
    }
}
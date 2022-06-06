package com.myapp.app.Contolers;

import com.myapp.app.Database.Hibernate.CRUD;
import com.myapp.app.Database.Tables.Timev;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class Hell {
    @FXML
    public TableView tables;
    @FXML
    public TableColumn<Timev, String> c1, c2, c3;

    @FXML
    private void initialize() {
        unloadingToTablesUsers();
    }

    private void unloadingToTablesUsers() {
        List<Timev> userList = CRUD.selectFromTimev();
        c1.setCellValueFactory((TableColumn.CellDataFeatures<Timev, String> cd) -> {
            return new SimpleStringProperty(cd.getValue().getLoginT());
        });
        c2.setCellValueFactory((TableColumn.CellDataFeatures<Timev, String> cd) -> {
            return new SimpleStringProperty(cd.getValue().getTime().toString());
        });
        c3.setCellValueFactory((TableColumn.CellDataFeatures<Timev, String> cd) -> {
            return new SimpleStringProperty(cd.getValue().getResult());
        });
        ObservableList<Timev> tableUser = FXCollections.observableList(userList);
        tables.setItems(tableUser);
    }
}

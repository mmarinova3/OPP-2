package com.winery.controllers;

import com.winery.entities.Bottle;
import com.winery.service.BottleService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class RefBottleTypeRefController {
    @FXML
    private TableView<Bottle> bottleTableView;
    @FXML
    private TableColumn<Bottle, Double> volumeColumn;
    @FXML
    private TableColumn<Bottle, Integer> quantityColumn;

    private final BottleService bottleService;

    public RefBottleTypeRefController() {
        this.bottleService = BottleService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    @FXML
    public void initialize() {

        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        List<Bottle> bottles = bottleService.getAll();
        bottleTableView.getItems().addAll(bottles);

        bottleTableView.getSortOrder().add(volumeColumn);
        bottleTableView.sort();

        bottleTableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Bottle item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty && item != null && item.getQuantity() < 50) {
                    setStyle("-fx-background-color: red;");
                } else {
                    setStyle("");
                }
            }
        });
    }
}

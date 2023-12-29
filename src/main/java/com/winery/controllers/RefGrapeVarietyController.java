package com.winery.controllers;

import com.winery.entities.GrapeCategory;
import com.winery.entities.GrapeVariety;
import com.winery.service.GrapeVarietyService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class RefGrapeVarietyController {
    @FXML
    private TableView<GrapeVariety> grapeVarietyTableView;
    @FXML
    private TableColumn<GrapeVariety, String> grapeNameColumn;
    @FXML
    private TableColumn<GrapeVariety, GrapeCategory> categoryColumn;
    @FXML
    private TableColumn<GrapeVariety, Double> quantityColumn;


    private final GrapeVarietyService grapeVarietyService;

    public RefGrapeVarietyController() {
        this.grapeVarietyService = GrapeVarietyService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }
    @FXML
    public void initialize() {

        grapeNameColumn.setCellValueFactory(new PropertyValueFactory<>("grapeName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        List<GrapeVariety> grapeVarieties = grapeVarietyService.getAll();
        grapeVarietyTableView.getItems().addAll(grapeVarieties);

        grapeVarietyTableView.getSortOrder().add(grapeNameColumn);
        grapeVarietyTableView.sort();

        grapeVarietyTableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(GrapeVariety item, boolean empty) {
                super.updateItem(item, empty);

                if (!empty && item != null && item.getQuantity() < 300) {
                    setStyle("-fx-background-color: red;");
                } else {
                    setStyle("");
                }
            }
        });
    }

}

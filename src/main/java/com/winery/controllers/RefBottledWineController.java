package com.winery.controllers;

import com.winery.entities.Bottle;
import com.winery.entities.BottledWine;
import com.winery.entities.WineComposition;
import com.winery.service.BottledWineService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.util.List;

public class RefBottledWineController {
    @FXML
    private TableView<BottledWine> bottledWineTableView;
    @FXML
    private TableColumn<BottledWine, String> wineNameColumn;
    @FXML
    private TableColumn<BottledWine, String> bottleTypeColumn;
    @FXML
    private TableColumn<BottledWine, Integer> usedBottlesNumColumn;
    @FXML
    private TableColumn<BottledWine, Date> dateColumn;

    private final BottledWineService bottledWineService;

    public RefBottledWineController() {
        this.bottledWineService = BottledWineService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }
    @FXML
    public void initialize() {

        wineNameColumn.setCellValueFactory(cellData -> {
            WineComposition wineComposition = cellData.getValue().getWineComposition();
            return new SimpleStringProperty(wineComposition != null ? wineComposition.getWineName() : "");
        });

        bottleTypeColumn.setCellValueFactory(cellData -> {
            Bottle bottle = cellData.getValue().getBottle();
            return new SimpleStringProperty(bottle != null ?bottle.getVolume().toString() : "");
        });

        usedBottlesNumColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("bottlingDate"));

        List<BottledWine> bottledWines = bottledWineService.getAll();
        bottledWineTableView.getItems().addAll(bottledWines);

    }

}

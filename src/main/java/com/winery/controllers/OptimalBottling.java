package com.winery.controllers;

import com.winery.entities.Bottle;
import com.winery.entities.BottledWine;
import com.winery.entities.WineComponents;
import com.winery.entities.WineComposition;
import com.winery.service.BottleService;
import com.winery.service.BottledWineService;
import com.winery.service.WineCompositionService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OptimalBottling {
    private Label eventMessage;

    private ComboBox<String> wineName;
    @FXML
    private ComboBox<Double> bottleVolume;
    @FXML
    private TextField quantityBottled;
    @FXML
    private DatePicker bottlingDate;

    private final BottledWineService bottledWineService;
    private final WineCompositionService wineCompositionService;
    private final BottleService bottleService;

    public OptimalBottling() {
        this.bottledWineService = BottledWineService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.wineCompositionService = WineCompositionService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.bottleService = BottleService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    @FXML
    public void initialize() {
        setComboBoxWine();
        setComboBoxBottle();

    }
    private void updateFieldsWithSelectedRow(BottledWine selectedBottledWine) {
        WineComposition wineComposition = selectedBottledWine.getWineComposition();
        wineName.setValue(wineComposition.getWineName());
        Bottle bottle= selectedBottledWine.getBottle();
        bottleVolume.setValue(bottle.getVolume());
        quantityBottled.setText(String.valueOf(selectedBottledWine.getQuantity()));
        bottlingDate.setValue(selectedBottledWine.getBottlingDate().toLocalDate());

    }

    private void clearInputFields() {
        wineName.getSelectionModel().clearSelection();
        bottleVolume.getSelectionModel().clearSelection();
        quantityBottled.clear();
        bottlingDate.setValue(null);
    }
    private void setComboBoxWine() {
        List<String> wineNames = wineCompositionService.getAll()
                .stream()
                .map(WineComposition::getWineName)
                .collect(Collectors.toList());

        wineName.setItems(FXCollections.observableArrayList(wineNames));
    }

    private void setComboBoxBottle() {
        List<Double> bottleVolumes =bottleService.getAll()
                .stream()
                .map(Bottle::getVolume)
                .collect(Collectors.toList());

        bottleVolume.setItems(FXCollections.observableArrayList(bottleVolumes));
    }

    @FXML
    private void optimalBottling() {
        String wine = wineName.getValue();
        Double volume = bottleVolume.getValue();
        String quantityText = quantityBottled.getText();

        if (wine == null || volume == null || quantityText.isEmpty()) {
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            eventMessage.setText("Quantity must be a valid number");
            return;
        }



        WineComposition wineComposition = new WineComposition();
        wineComposition.setWineName(wine);
        Integer wineId = wineCompositionService.findIdByName(wineComposition.getWineName());
        wineComposition.setId(wineId);

        WineComponents wineComponents = new WineComponents();
        wineComponents.setWineComposition(wineComposition);






        Bottle bottle = new Bottle();
        bottle.setVolume(volume);
        Integer bottleId = bottleService.findIdByVolume(bottle.getVolume());
        bottle.setId(bottleId);

        BottledWine bottledWine = new BottledWine();
        bottledWine.setWineComposition(wineComposition);
        bottledWine.setBottle(bottle);
        bottledWine.setQuantity(quantity);
        clearInputFields();
        eventMessage.setText("Bottling added successfully");

        bottledWineService.save(bottledWine);
    }

}

package com.winery.controllers;

import com.winery.entities.*;
import com.winery.service.*;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;


import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BottlingController {

    @FXML
    private ComboBox<String> wineName;
    @FXML
    private ComboBox<Double> bottleVolume;
    @FXML
    private TextField quantityBottled;
    @FXML
    private DatePicker bottlingDate;

    private BottledWineService bottledWineService;
    private WineCompositionService wineCompositionService;
    private BottleService bottleService;

    public BottlingController() {
        this.bottledWineService = BottledWineService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.wineCompositionService = WineCompositionService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.bottleService = BottleService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    @FXML
    public void initialize() {
        setComboBoxWine();
        setComboBoxBottle();
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
    private void defineWineComponents() {
        String wine = wineName.getValue();
        Double volume = bottleVolume.getValue();
        int quantity = Integer.parseInt(quantityBottled.getText());
        Date date = Date.valueOf(bottlingDate.getValue());

        if (wine == null || volume == null || quantity == 0 || date == null) {
            return;
        }

        WineComposition wineComposition = new WineComposition();
        wineComposition.setWineName(wine);
        Integer wineId = wineCompositionService.findIdByName(wineComposition.getWineName());
        wineComposition.setId(wineId);

        Bottle bottle = new Bottle();
        bottle.setVolume(volume);
        Integer bottleId = bottleService.findIdByVolume(bottle.getVolume());
        bottle.setId(bottleId);

        BottledWine bottledWine = new BottledWine();
        bottledWine.setWineComposition(wineComposition);
        bottledWine.setBottle(bottle);
        bottledWine.setQuantity(quantity);
        bottledWine.setBottlingDate(date);

        // Calculate optimal bottle filling
        double remainingVolume = bottle.getVolume() * quantity;

        // Assuming you have a method that returns the optimal quantity to fill the bottles
        int optimalQuantity = calculateOptimalQuantity(bottle, remainingVolume);

        // Update the quantity with the calculated optimal quantity
        bottledWine.setQuantity(optimalQuantity);

        bottledWineService.save(bottledWine);
    }

    private int calculateOptimalQuantity(Bottle bottle, double remainingVolume) {
        // Assuming a simple calculation based on the bottle volume
        return (int) (remainingVolume / bottle.getVolume());
    }

}

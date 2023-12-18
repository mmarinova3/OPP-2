package com.winery.controllers;

import com.winery.entities.Bottle;
import com.winery.service.BottleService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class BottleRegisterController {

    @FXML
    private TextField volume;
    @FXML
    private TextField quantity;

    private BottleService bottleService;

    public BottleRegisterController() {
        this.bottleService = BottleService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    @FXML
    private void registerBottle() {
        double newVolume = Double.parseDouble(volume.getText());
        int newQuantity = Integer.parseInt(quantity.getText());

        if ( newVolume == 0 || newQuantity == 0) {
            return;
        }

        if (bottleService.existsByVolume(newVolume)) {
            System.out.println("A bottle with volume " + newVolume + " already exists!");
            return;
        }

        Bottle bottle = new Bottle();
        bottle.setVolume(newVolume);
        bottle.setQuantity(newQuantity);


       bottleService.save(bottle);
    }



}

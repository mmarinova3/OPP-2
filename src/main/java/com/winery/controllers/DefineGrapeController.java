package com.winery.controllers;

import com.winery.entities.*;
import com.winery.service.GrapeVarietyService;
import com.winery.service.WineYieldService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class DefineGrapeController {

    @FXML
    private ComboBox<String> grapeVariety;
    @FXML
    private TextField yieldPerKG;

    private WineYieldService wineYieldService;
    private GrapeVarietyService grapeVarietyService;

    public DefineGrapeController() {
        this.wineYieldService = WineYieldService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.grapeVarietyService = GrapeVarietyService.getInstance(Connection.getEntityManager(), Session.getInstance());

    }

    @FXML
    public void initialize() {
        setComboBoxItems();
    }

    private void setComboBoxItems() {
        List<String> grapeNames = grapeVarietyService.getAll()
                .stream()
                .map(GrapeVariety::getGrapeName)
                .collect(Collectors.toList());

        grapeVariety.setItems(FXCollections.observableArrayList(grapeNames));
    }

    @FXML
    private void registerGrapeVariety() {
        String grapeName = grapeVariety.getValue();
        double yield= Double.parseDouble(yieldPerKG.getText());

        if (grapeName.isEmpty() ||  yield == 0) {
            return;
        }

        GrapeVariety grapeVariety = new GrapeVariety();
        grapeVariety.setGrapeName(grapeName);
        Integer grapeId = grapeVarietyService.findIdByName(grapeVariety.getGrapeName());
        grapeVariety.setId(grapeId);

        WineYield wineYield = new WineYield();
        wineYield.setGrape(grapeVariety);
        wineYield.setYieldPerKg(yield);

        System.out.print("Name"+ grapeVariety.getGrapeName()+" Yield"+yield);
        wineYieldService.saveProduction(wineYield);
    }
}

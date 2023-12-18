package com.winery.controllers;

import com.winery.entities.GrapeVariety;
import com.winery.entities.WineComposition;
import com.winery.entities.WineComponents;
import com.winery.service.GrapeVarietyService;
import com.winery.service.WineComponentsService;
import com.winery.service.WineCompositionService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class WineComponentsController {
    @FXML
    private ComboBox<String> wineName;
    @FXML
    private ComboBox<String> grapeName;
    @FXML
    private TextField quantityNeeded;

    private WineComponentsService wineComponentsService;
    private WineCompositionService wineCompositionService;
    private GrapeVarietyService grapeVarietyService;

    public WineComponentsController() {
        this.wineComponentsService = WineComponentsService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.wineCompositionService = WineCompositionService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.grapeVarietyService = GrapeVarietyService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    @FXML
    public void initialize() {
        setComboBoxWine();
        setComboBoxGrape();
    }

    private void setComboBoxWine() {
        List<String> wineNames = wineCompositionService.getAll()
                .stream()
                .map(WineComposition::getWineName)
                .collect(Collectors.toList());

        wineName.setItems(FXCollections.observableArrayList(wineNames));
    }

    private void setComboBoxGrape() {
        List<String> grapeNames = grapeVarietyService.getAll()
                .stream()
                .map(GrapeVariety::getGrapeName)
                .collect(Collectors.toList());

        grapeName.setItems(FXCollections.observableArrayList(grapeNames));
    }

    @FXML
    private void defineWineComponents() {
        String wine = wineName.getValue();
        String grape = grapeName.getValue();
        double quantity = Double.parseDouble(quantityNeeded.getText());

        if ( wine == null ||grape == null || quantity == 0 ){
            return;
        }

        WineComposition wineComposition = new WineComposition();
        wineComposition.setWineName(wine);
        Integer wineId = wineCompositionService.findIdByName(wineComposition.getWineName());
        wineComposition.setId(wineId);

        GrapeVariety grapeVariety = new GrapeVariety();
        grapeVariety.setGrapeName(grape);
        Integer grapeId = grapeVarietyService.findIdByName(grapeVariety.getGrapeName());
        grapeVariety.setId(grapeId);

        WineComponents wineComponents = new WineComponents();
        wineComponents.setWineComposition(wineComposition);
        wineComponents.setGrape(grapeVariety);
        wineComponents.setQuantityNeeded(quantity);

        wineComponentsService.save(wineComponents);
    }

}

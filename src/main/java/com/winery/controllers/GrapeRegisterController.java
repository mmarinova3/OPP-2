package com.winery.controllers;

import com.winery.entities.*;
import com.winery.service.GrapeVarietyService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GrapeRegisterController {

    @FXML
    private TextField newGrapeName;
    @FXML
    private TextField newQuantity;

    @FXML
    private ComboBox<String> newCategory;
    private GrapeVarietyService grapeVarietyService;

    public GrapeRegisterController() {
        this.grapeVarietyService = GrapeVarietyService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    @FXML
    public void initialize() {
        setComboBoxItems();
    }

    private void setComboBoxItems() {

        List<String> grapeCategories = Arrays.stream(GrapeCategory.values())
                .map(GrapeCategory::name)
                .collect(Collectors.toList());

        ObservableList<String> grapeCategoryItems = FXCollections.observableArrayList(grapeCategories);

        newCategory.setItems(grapeCategoryItems);
    }

    @FXML
    private void registerGrapeVariety() {
        String grapeName = newGrapeName.getText();
        String category = newCategory.getValue();
        double quantity = Double.parseDouble(newQuantity.getText());

        if (grapeName.isEmpty() || category == null || quantity == 0) {
            return;
        }

        GrapeVariety grapeVariety = new GrapeVariety();
        grapeVariety.setGrapeName(grapeName);
        grapeVariety.setCategory(GrapeCategory.valueOf(category));
        grapeVariety.setQuantity(quantity);

        System.out.println(grapeVariety.getGrapeName()+grapeVariety.getCategory()+grapeVariety.getQuantity());
        grapeVarietyService.saveUser(grapeVariety);
    }
}

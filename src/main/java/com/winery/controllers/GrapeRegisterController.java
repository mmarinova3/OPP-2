package com.winery.controllers;

import com.winery.accessControl.AccessController;
import com.winery.entities.*;
import com.winery.service.GrapeVarietyService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GrapeRegisterController {

    @FXML
    private Label eventMessage;
    @FXML
    private Tooltip eventMessageTooltip;
    @FXML
    private TextField newGrapeName;
    @FXML
    private TextField newQuantity;
    @FXML
    private ComboBox<String> newCategory;
    @FXML
    private TableView<GrapeVariety> grapeVarietyTableView;
    @FXML
    private TableColumn<GrapeVariety, Integer> idColumn;
    @FXML
    private TableColumn<GrapeVariety, String> grapeNameColumn;
    @FXML
    private TableColumn<GrapeVariety, GrapeCategory> categoryColumn;
    @FXML
    private TableColumn<GrapeVariety, Double> quantityColumn;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private final AccessController accessController;
    private final GrapeVarietyService grapeVarietyService;

    public GrapeRegisterController() {
        this.grapeVarietyService = GrapeVarietyService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.accessController = new AccessController(Session.getInstance().getUser());
    }

    @FXML
    public void initialize() {
        setComboBoxItems();
        accessCheck();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        grapeNameColumn.setCellValueFactory(new PropertyValueFactory<>("grapeName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        List<GrapeVariety> grapeVarieties = grapeVarietyService.getAll();
        grapeVarieties.sort(Comparator.comparing(GrapeVariety::getGrapeName));
        grapeVarietyTableView.getItems().addAll(grapeVarieties);

        grapeVarietyTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFieldsWithSelectedRow(newValue);
            } else {
                clearInputFields();
            }
        });
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
        double quantity;

        try {
            quantity = Double.parseDouble(newQuantity.getText());
        } catch (NumberFormatException e) {
            eventMessage.setText("Invalid quantity format");
            eventMessageTooltip.setText(eventMessage.getText());
            return;
        }

        if (grapeName.isEmpty() || category == null || quantity == 0) {
            eventMessage.setText("Please fill in all fields");
            eventMessageTooltip.setText(eventMessage.getText());
            return;
        }

        GrapeVariety grapeVariety = new GrapeVariety();
        grapeVariety.setGrapeName(grapeName);
        grapeVariety.setCategory(GrapeCategory.valueOf(category));
        grapeVariety.setQuantity(quantity);

        grapeVarietyService.save(grapeVariety);
        grapeVarietyTableView.getItems().add(grapeVariety);
        clearInputFields();
        eventMessage.setText("New grape variety successfully added");
        eventMessageTooltip.setText(eventMessage.getText());

    }


    private void updateFieldsWithSelectedRow(GrapeVariety selectedGrapeVariety) {
        newGrapeName.setText(selectedGrapeVariety.getGrapeName());
        newCategory.setValue(selectedGrapeVariety.getCategory().name());
        newQuantity.setText(String.valueOf(selectedGrapeVariety.getQuantity()));
    }

    private void clearInputFields() {
        newGrapeName.clear();
        newCategory.getSelectionModel().clearSelection();
        newQuantity.clear();
    }

    @FXML
    private void updateSelectedGrapeVariety() {
        GrapeVariety selectedGrapeVariety = grapeVarietyTableView.getSelectionModel().getSelectedItem();

        if (selectedGrapeVariety != null) {
            String updatedName = newGrapeName.getText();
            String updatedCategory = newCategory.getValue();
            String updatedQuantityText = newQuantity.getText();

            if (updatedName.isEmpty() || updatedCategory == null || updatedQuantityText.isEmpty()) {
                eventMessage.setText("Please fill in all fields for the update");
                eventMessageTooltip.setText(eventMessage.getText());
                return;
            }

            try {
                GrapeCategory category = GrapeCategory.valueOf(updatedCategory);
                double updatedQuantity = Double.parseDouble(updatedQuantityText);

                selectedGrapeVariety.setGrapeName(updatedName);
                selectedGrapeVariety.setCategory(category);
                selectedGrapeVariety.setQuantity(updatedQuantity);

                grapeVarietyService.update(selectedGrapeVariety, new String[]{updatedName, String.valueOf(category), String.valueOf(updatedQuantity)});
                grapeVarietyTableView.refresh();
                eventMessage.setText("Grape variety successfully updated");
                eventMessageTooltip.setText(eventMessage.getText());

            } catch (NumberFormatException e) {
                eventMessage.setText("Error: Invalid quantity format");
                eventMessageTooltip.setText(eventMessage.getText());

            } catch (IllegalArgumentException e) {
                eventMessage.setText("Error: Invalid grape category");
                eventMessageTooltip.setText(eventMessage.getText());

            }
        } else {
            eventMessage.setText("Please select a row to update");
            eventMessageTooltip.setText(eventMessage.getText());

        }
    }

    @FXML
    private void deleteSelectedGrapeVariety() {
        GrapeVariety selectedGrapeVariety = grapeVarietyTableView.getSelectionModel().getSelectedItem();

        if (selectedGrapeVariety != null) {
            try {
                grapeVarietyService.delete(selectedGrapeVariety);
                grapeVarietyTableView.getItems().remove(selectedGrapeVariety);
                grapeVarietyTableView.refresh();
                eventMessage.setText("Grape variety successfully deleted");
                eventMessageTooltip.setText(eventMessage.getText());
            } catch (EntityNotFoundException e) {
                eventMessage.setText("Cannot delete the grape variety. It does not exist.");
                eventMessageTooltip.setText(eventMessage.getText());
            } catch (PersistenceException e) {
                eventMessage.setText("An error occurred during the deletion process.");
                eventMessageTooltip.setText(eventMessage.getText());
            } catch (Exception e) {
                eventMessage.setText("An unexpected error occurred while deleting the grape variety.");
                eventMessageTooltip.setText(eventMessage.getText());
            }
        } else {
            eventMessage.setText("Please select a row to delete");
            eventMessageTooltip.setText(eventMessage.getText());
        }
    }



    private void accessCheck(){
       if(!accessController.checkAdminOrOperatorAccess()){
           editButton.setDisable(true);
           deleteButton.setDisable(true);
       }
    }
}

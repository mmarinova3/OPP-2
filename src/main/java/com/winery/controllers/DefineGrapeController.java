package com.winery.controllers;

import com.winery.accessControl.AccessController;
import com.winery.entities.*;
import com.winery.service.GrapeVarietyService;
import com.winery.service.WineYieldService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefineGrapeController {
    @FXML
    private Label eventMessage;
    @FXML
    private Tooltip eventMessageTooltip;
    @FXML
    private Text showLitersInStock;
    @FXML
    private ComboBox<String> grapeVarietyComboBox;
    @FXML
    private TextField yieldPerKG;
    @FXML
    private TableView<WineYield> wineYieldTableView;
    @FXML
    private TableColumn<WineYield, String> grapeNameColum;
    @FXML
    private TableColumn<WineYield, Double> litersColumn;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    private final AccessController accessController;
    private final WineYieldService wineYieldService;
    private final GrapeVarietyService grapeVarietyService;


    public DefineGrapeController() {
        this.wineYieldService = WineYieldService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.grapeVarietyService = GrapeVarietyService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.accessController = new AccessController(Session.getInstance().getUser());
    }

    @FXML
    public void initialize() {
        setComboBoxItems();
        accessCheck();

        grapeNameColum.setCellValueFactory(cellData -> {
            GrapeVariety grape = cellData.getValue().getGrape();
            return new SimpleStringProperty(grape != null ? grape.getGrapeName() : "");
        });

        litersColumn.setCellValueFactory(new PropertyValueFactory<>("yieldPerKg"));

        List<WineYield> wineYields = wineYieldService.getAll();
        wineYields.sort(Comparator.comparing(wineYield -> wineYield.getGrape().getGrapeName()));
        wineYieldTableView.getItems().addAll(wineYields);

        wineYieldTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFieldsWithSelectedRow(newValue);
                getLitersWineInStock();
            } else {
                clearInputFields();
            }
        });


    }

    private void updateFieldsWithSelectedRow(WineYield selectedWineYield) {
        GrapeVariety selectedGrape = selectedWineYield.getGrape();
        grapeVarietyComboBox.setValue(selectedGrape.getGrapeName());
        yieldPerKG.setText(String.valueOf(selectedWineYield.getYieldPerKg()));
    }


    private void clearInputFields() {
       grapeVarietyComboBox.getSelectionModel().clearSelection();
       yieldPerKG.clear();
    }

    private void setComboBoxItems() {
        List<String> grapeNames = grapeVarietyService.getAll()
                .stream()
                .map(GrapeVariety::getGrapeName)
                .collect(Collectors.toList());

        grapeVarietyComboBox.setItems(FXCollections.observableArrayList(grapeNames));
    }

    @FXML
    private void defineGrapeVariety() {
        String grapeName = grapeVarietyComboBox.getValue();
        double yield;

        try {
            yield = Double.parseDouble(yieldPerKG.getText());
        } catch (NumberFormatException e) {
            eventMessage.setText("Invalid format");
            eventMessageTooltip.setText(eventMessage.getText());
            return;
        }

        if (grapeName.isEmpty() ||  yield == 0) {
            eventMessage.setText("Please fill in all fields");
            eventMessageTooltip.setText(eventMessage.getText());

            return;
        }

        GrapeVariety grapeVariety = new GrapeVariety();
        grapeVariety.setGrapeName(grapeName);
        Integer grapeId = grapeVarietyService.findIdByName(grapeVariety.getGrapeName());
        grapeVariety.setId(grapeId);


        boolean grapeExists = wineYieldTableView.getItems().stream()
                .anyMatch(wineYield -> wineYield.getGrape().getGrapeName().equals(grapeName));


        if (grapeExists) {
            eventMessage.setText("Yield for the selected grape variety is already defined");
            eventMessageTooltip.setText(eventMessage.getText());

            return;
        }

        WineYield wineYield = new WineYield();
        wineYield.setGrape(grapeVariety);
        wineYield.setYieldPerKg(yield);

        System.out.print("Name"+ grapeVariety.getGrapeName()+" Yield"+yield);
        wineYieldService.save(wineYield);
        wineYieldTableView.getItems().add(wineYield);
        clearInputFields();
        eventMessage.setText("New grape variety yield successfully added");
        eventMessageTooltip.setText(eventMessage.getText());

    }

    @FXML
    private void updateSelectedWineYield() {
        WineYield selectedWineYield = wineYieldTableView.getSelectionModel().getSelectedItem();

        if (selectedWineYield != null) {
            String grapeVarietyName = grapeVarietyComboBox.getValue();
            String updatedYieldPerKgText = yieldPerKG.getText();

            if (updatedYieldPerKgText.isEmpty()) {
                eventMessage.setText("Please fill in all fields for the update");
                eventMessageTooltip.setText(eventMessage.getText());

                return;
            }
            if (!Objects.equals(grapeVarietyName, selectedWineYield.getGrape().getGrapeName())) {
                eventMessage.setText("Grape variety cannot be changed");
                eventMessageTooltip.setText(eventMessage.getText());

                return;
            }
            try {
                double updatedYieldPerKg = Double.parseDouble(updatedYieldPerKgText);
                selectedWineYield.setYieldPerKg(updatedYieldPerKg);

                wineYieldService.update(selectedWineYield, new String[]{grapeVarietyName, updatedYieldPerKgText});
                wineYieldTableView.refresh();
                eventMessage.setText("Successfully updated");
                eventMessageTooltip.setText(eventMessage.getText());

            } catch (NumberFormatException e) {
                eventMessage.setText("Error: Invalid format");
                eventMessageTooltip.setText(eventMessage.getText());

            }
        } else {
            eventMessage.setText("Please select a row to update");
            eventMessageTooltip.setText(eventMessage.getText());

        }
    }

    @FXML
    private void deleteSelectedWineYield() {

        WineYield selectedWineYield = wineYieldTableView.getSelectionModel().getSelectedItem();

        if (selectedWineYield != null) {
            try{
              wineYieldService.delete(selectedWineYield);
              wineYieldTableView.getItems().remove(selectedWineYield);
              wineYieldTableView.refresh();
              eventMessage.setText("Successfully deleted");
                eventMessageTooltip.setText(eventMessage.getText());

            } catch (EntityNotFoundException e) {
                eventMessage.setText("Cannot delete the definition. It does not exist.");
                eventMessageTooltip.setText(eventMessage.getText());

            } catch (PersistenceException e) {
                eventMessage.setText("An error occurred during the deletion process.");
                eventMessageTooltip.setText(eventMessage.getText());

            } catch (Exception e) {
                eventMessage.setText("An unexpected error occurred while deleting the definition.");
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


    public void getLitersWineInStock() {
        GrapeVariety grapeVariety = new GrapeVariety();
        WineYield wineYield = wineYieldTableView.getSelectionModel().getSelectedItem();
        grapeVariety.setId(wineYield.getGrape().getId());

        double result = grapeVarietyService.findQuantityById(grapeVariety.getId()) * wineYield.getYieldPerKg();
        double roundedResult = Math.round(result * 100.0) / 100.0;

        showLitersInStock.setText("There is " + roundedResult + " liters of " + wineYield.getGrape().getGrapeName());
    }


}

package com.winery.controllers;

import com.winery.accessControl.AccessController;
import com.winery.entities.GrapeVariety;
import com.winery.entities.WineComposition;
import com.winery.entities.WineComponents;
import com.winery.service.GrapeVarietyService;
import com.winery.service.WineComponentsService;
import com.winery.service.WineCompositionService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WineComponentsController {
    @FXML
    private Label eventMessage;
    @FXML
    private Tooltip eventMessageTooltip;
    @FXML
    private ComboBox<String> wineName;
    @FXML
    private ComboBox<String> grapeName;
    @FXML
    private TextField quantityNeeded;
    @FXML
    private TableView<WineComponents> wineComponentsTableView;
    @FXML
    private TableColumn<WineComponents, String> wineNameColumn;
    @FXML
    private TableColumn<WineComponents, String> grapeNameColumn;
    @FXML
    private TableColumn<WineComponents, Double> quantityNeededColumn;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    private final AccessController accessController;
    private final WineComponentsService wineComponentsService;
    private final WineCompositionService wineCompositionService;
    private final GrapeVarietyService grapeVarietyService;

    public WineComponentsController() {
        this.wineComponentsService = WineComponentsService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.wineCompositionService = WineCompositionService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.grapeVarietyService = GrapeVarietyService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.accessController = new AccessController(Session.getInstance().getUser());
    }

    @FXML
    public void initialize() {

        setComboBoxWine();
        setComboBoxGrape();
        accessCheck();

        wineNameColumn.setCellValueFactory(cellData -> {
            WineComposition wineComposition = cellData.getValue().getWineComposition();
            return new SimpleStringProperty(wineComposition != null ? wineComposition.getWineName() : "");
        });
        grapeNameColumn.setCellValueFactory(cellData -> {
            GrapeVariety grape = cellData.getValue().getGrape();
            return new SimpleStringProperty(grape != null ? grape.getGrapeName() : "");
        });
        quantityNeededColumn.setCellValueFactory(new PropertyValueFactory<>("quantityNeeded"));

        List<WineComponents> wineComponents= wineComponentsService.getAll();
        wineComponents.sort(Comparator.comparing(wineComponent-> wineComponent.getWineComposition().getWineName()));
        wineComponentsTableView.getItems().addAll(wineComponents);

        wineComponentsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFieldsWithSelectedRow(newValue);
            } else {
                clearInputFields();
            }
        });
        wineComponentsTableView.getSortOrder().add(wineNameColumn);
        wineComponentsTableView.sort();

    }
    private void updateFieldsWithSelectedRow(WineComponents selectedWineComponents) {
        WineComposition selectedComposition = selectedWineComponents.getWineComposition();
        wineName.setValue(selectedComposition.getWineName());
        GrapeVariety selectedGrape = selectedWineComponents.getGrape();
        grapeName.setValue(selectedGrape.getGrapeName());
        quantityNeeded.setText(String.valueOf(selectedWineComponents.getQuantityNeeded()));
    }

    private void clearInputFields() {
        wineName.getSelectionModel().clearSelection();
        grapeName.getSelectionModel().clearSelection();
        quantityNeeded.clear();
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
        double quantity;

        try{
              quantity = Double.parseDouble(quantityNeeded.getText());
        } catch (NumberFormatException e) {
            eventMessage.setText("Invalid format");
            eventMessageTooltip.setText(eventMessage.getText());
            return;
        }

        if ( wine == null ||grape == null || quantity == 0 ){
            eventMessage.setText("Please fill in all fields");
            eventMessageTooltip.setText(eventMessage.getText());
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

        boolean compositionExists = wineComponentsTableView.getItems().stream()
                .anyMatch(wineComponent ->
                        wineComponent.getWineComposition().getWineName().equals(wine) &&
                                wineComponent.getGrape().getGrapeName().equals(grape));

        if (compositionExists) {
            eventMessage.setText("The composition already exists");
            eventMessageTooltip.setText(eventMessage.getText());
            return;
        }

        WineComponents wineComponents = new WineComponents();
        wineComponents.setWineComposition(wineComposition);
        wineComponents.setGrape(grapeVariety);
        wineComponents.setQuantityNeeded(quantity);

        wineComponentsService.save(wineComponents);
        wineComponentsTableView.getItems().add(wineComponents);
        clearInputFields();
        eventMessage.setText("New component added successfully");
        eventMessageTooltip.setText(eventMessage.getText());
    }

    @FXML
    private void updateSelectedWineComponent() {
        WineComponents selectedWineComponents = wineComponentsTableView.getSelectionModel().getSelectedItem();

        if (selectedWineComponents != null) {
            String wine = wineName.getValue();
            String grape = grapeName.getValue();
            String quantityText = quantityNeeded.getText();

            if (wine.isEmpty() && grape.isEmpty() && quantityText.isEmpty()) {
                eventMessage.setText("Please fill in all fields for the update.");
                eventMessageTooltip.setText(eventMessage.getText());
                return;
            }

            if (!Objects.equals(selectedWineComponents.getWineComposition().getWineName(), wine) || !Objects.equals(selectedWineComponents.getGrape().getGrapeName(), grape)) {
                eventMessage.setText("Can not change the composition and the grape.");
                eventMessageTooltip.setText(eventMessage.getText());
                return;
            }

            try {
                double quantity = Double.parseDouble(quantityText);
                selectedWineComponents.setQuantityNeeded(quantity);
            } catch (NumberFormatException e) {
                eventMessage.setText("Invalid quantity format.");
                eventMessageTooltip.setText(eventMessage.getText());
                return;
            }

            wineComponentsService.update(selectedWineComponents, new String[]{wine, grape, quantityText});
            wineComponentsTableView.refresh();
            eventMessage.setText("Successfully updated");
            eventMessageTooltip.setText(eventMessage.getText());

        } else {
            eventMessage.setText("Please select a row to update");
            eventMessageTooltip.setText(eventMessage.getText());
        }
    }


    @FXML
    private void deleteSelectedWineComponent() {

        WineComponents selectedWineComponents = wineComponentsTableView.getSelectionModel().getSelectedItem();

        if (selectedWineComponents != null) {
            try{
                wineComponentsService.delete(selectedWineComponents);
                wineComponentsTableView.getItems().remove(selectedWineComponents);
                wineComponentsTableView.refresh();
                eventMessage.setText("Successfully deleted");
                eventMessageTooltip.setText(eventMessage.getText());
            } catch (EntityNotFoundException e) {
                eventMessage.setText("Cannot delete the wine component. It does not exist.");
                eventMessageTooltip.setText(eventMessage.getText());
            } catch (PersistenceException e) {
                eventMessage.setText("An error occurred during the deletion process.");
                eventMessageTooltip.setText(eventMessage.getText());
            } catch (Exception e) {
                eventMessage.setText("An unexpected error occurred while deleting the wine component.");
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

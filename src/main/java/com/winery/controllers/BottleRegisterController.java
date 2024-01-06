package com.winery.controllers;

import com.winery.accessControl.AccessController;
import com.winery.entities.Bottle;
import com.winery.service.BottleService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class BottleRegisterController {
    @FXML
    private Label eventMessage;
    @FXML
    private TextField volume;
    @FXML
    private TextField quantity;
    @FXML
    private TableView<Bottle> bottleTableView;
    @FXML
    private TableColumn<Bottle, Double> volumeColumn;
    @FXML
    private TableColumn<Bottle, Integer> quantityColumn;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    private final AccessController accessController;
    private final BottleService bottleService;

    public BottleRegisterController() {
        this.bottleService = BottleService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.accessController = new AccessController(Session.getInstance().getUser());
    }

    @FXML
    public void initialize() {
        accessCheck();

        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        List<Bottle> bottles = bottleService.getAll();
        bottleTableView.getItems().addAll(bottles);
        bottleTableView.refresh();

        bottleTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFieldsWithSelectedRow(newValue);
            } else {
                clearInputFields();
            }
        });
    }


    private void updateFieldsWithSelectedRow(Bottle selectedBottle) {
        volume.setText(String.valueOf(selectedBottle.getVolume()));
        quantity.setText(String.valueOf(selectedBottle.getQuantity()));
    }

    private void clearInputFields() {
        volume.clear();
        quantity.clear();
    }

    @FXML
    private void registerBottle() {
        double newVolume = Double.parseDouble(volume.getText());
        int newQuantity = Integer.parseInt(quantity.getText());

        if ( newVolume == 0 || newQuantity == 0) {
            eventMessage.setText("Please fill in all fields");
            return;
        }

        if (bottleService.existsByVolume(newVolume)) {
           eventMessage.setText("A bottle with volume " + newVolume + " already exists!");
            return;
        }

        Bottle bottle = new Bottle();
        bottle.setVolume(newVolume);
        bottle.setQuantity(newQuantity);
        bottleService.save(bottle);
        bottleTableView.getItems().add(bottle);
        clearInputFields();
        eventMessage.setText("Bottle type successfully added");
    }

    @FXML
    private void updateSelectedBottle() {
        Bottle selectedBottle = bottleTableView.getSelectionModel().getSelectedItem();

        if (selectedBottle != null) {
            String volumeText = volume.getText();
            String quantityText = quantity.getText();

            if (volumeText.isEmpty() || quantityText.isEmpty()) {
                eventMessage.setText("Please fill in all fields for the update");
                return;
            }

            try {
                double updatedVolume = Double.parseDouble(volumeText);
                int updatedQuantity = Integer.parseInt(quantityText);

                if (updatedVolume != selectedBottle.getVolume()) {
                    eventMessage.setText("Volume cannot be changed");
                    return;
                }

                selectedBottle.setQuantity(updatedQuantity);

                bottleService.update(selectedBottle, new String[]{String.valueOf(updatedQuantity)});
                bottleTableView.refresh();
                eventMessage.setText("Bottle successfully updated");
            } catch (NumberFormatException e) {
                eventMessage.setText("Error: Invalid quantity format");
            }
        } else {
            eventMessage.setText("Please select a row to update");
        }
    }


    @FXML
    private void deleteSelectedBottle() {

        Bottle selectedBottle = bottleTableView.getSelectionModel().getSelectedItem();

        if (selectedBottle != null) {
            try{
               bottleService.delete(selectedBottle);
               bottleTableView.getItems().remove(selectedBottle);
               bottleTableView.refresh();
               eventMessage.setText("Bottle successfully deleted");
            } catch (EntityNotFoundException e) {
                eventMessage.setText("Cannot delete the bottle. It does not exist.");
            } catch (PersistenceException e) {
                eventMessage.setText("An error occurred during the deletion process.");
            } catch (Exception e) {
                eventMessage.setText("An unexpected error occurred while deleting the bottle.");
            }
        } else {
            eventMessage.setText("Please select a row to delete");
        }
    }
    private void accessCheck(){
        if(!accessController.checkAdminOrOperatorAccess()){
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        }
    }
}

package com.winery.controllers;

import com.winery.accessControl.AccessController;
import com.winery.entities.WineComposition;
import com.winery.service.WineCompositionService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class CompositionRegisterController {
    @FXML
    private Label eventMessage;
    @FXML
    private Tooltip eventMessageTooltip;
    @FXML
    private TextField newCompositionName;
    @FXML
    private TableView<WineComposition> wineCompositionTableView;
    @FXML
    private TableColumn<WineComposition, String> nameColumn;
    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    private final AccessController accessController;
    private final WineCompositionService wineCompositionService;

    public CompositionRegisterController() {
        this.wineCompositionService = WineCompositionService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.accessController = new AccessController(Session.getInstance().getUser());
    }
    @FXML
    public void initialize() {
        accessCheck();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("wineName"));

        List<WineComposition> wineCompositions = wineCompositionService.getAll();
       wineCompositionTableView.getItems().addAll(wineCompositions);

       wineCompositionTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFieldsWithSelectedRow(newValue);
            } else {
                clearInputFields();
            }
       });
    }

    private void updateFieldsWithSelectedRow(WineComposition selectedWineComposition) {
        newCompositionName.setText(String.valueOf(selectedWineComposition.getWineName()));
    }

    private void clearInputFields() {
        newCompositionName.clear();
    }

    @FXML
    public void registerComposition(){
       String compositionName= newCompositionName.getText();

       if (compositionName==null){
           eventMessage.setText("Please fill in all fields");
           eventMessageTooltip.setText(eventMessage.getText());
           return;
       }

        boolean nameExists = wineCompositionTableView.getItems().stream()
                .anyMatch(wineComposition -> wineComposition.getWineName().equals(compositionName));


        if (nameExists) {
            eventMessage.setText(compositionName+" already exists");
            eventMessageTooltip.setText(eventMessage.getText());
            return;
        }

       WineComposition wineComposition = new WineComposition();
       wineComposition.setWineName(compositionName);
       wineCompositionService.save(wineComposition);
       clearInputFields();
       wineCompositionTableView.getItems().add(wineComposition);
       wineCompositionTableView.refresh();
       eventMessage.setText("New wine composition successfully added");
       eventMessageTooltip.setText(eventMessage.getText());
    }

    @FXML
    private void updateSelectedWineComposition() {
        WineComposition selectedWineComposition = wineCompositionTableView.getSelectionModel().getSelectedItem();

        if (selectedWineComposition != null) {
            String compositionName = newCompositionName.getText();


            if (compositionName.isEmpty()) {
                eventMessage.setText("Please fill in field for the update");
                eventMessageTooltip.setText(eventMessage.getText());
                return;
            }

            boolean nameExists = wineCompositionTableView.getItems().stream()
                    .anyMatch(wineComposition -> wineComposition.getWineName().equals(compositionName));


            if (nameExists) {
                eventMessage.setText(compositionName+" already exists");
                eventMessageTooltip.setText(eventMessage.getText());
                return;
            }

            selectedWineComposition.setWineName(compositionName);

               wineCompositionService.update(selectedWineComposition, new String[]{compositionName});
               wineCompositionTableView.refresh();
               eventMessage.setText("Wine composition successfully updated");
               eventMessageTooltip.setText(eventMessage.getText());
        } else {
            eventMessage.setText("Please select a row to update");
            eventMessageTooltip.setText(eventMessage.getText());
        }
    }

    @FXML
    private void deleteSelectedBottle() {

        WineComposition selectedWineComposition = wineCompositionTableView.getSelectionModel().getSelectedItem();

        if (selectedWineComposition != null) {
            try{
              wineCompositionService.delete(selectedWineComposition);
              wineCompositionTableView.getItems().remove(selectedWineComposition);
              wineCompositionTableView.refresh();
              eventMessage.setText("Wine composition successfully deleted");
              eventMessageTooltip.setText(eventMessage.getText());
            } catch (EntityNotFoundException e) {
                eventMessage.setText("Cannot delete the wine composition. It does not exist.");
                eventMessageTooltip.setText(eventMessage.getText());
            } catch (PersistenceException e) {
                eventMessage.setText("An error occurred during the deletion process.");
                eventMessageTooltip.setText(eventMessage.getText());
            } catch (Exception e) {
                eventMessage.setText("An unexpected error occurred while deleting the wine composition.");
                eventMessageTooltip.setText(eventMessage.getText());
            }
        } else {
            eventMessage.setText("Please select a row to delete");
            eventMessageTooltip.setText(eventMessage.getText());
        }

    }

    @FXML
    public void openDefineWineComponentsView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/winery/winery_prod/input-fxml/define-components.fxml"));
        Parent root = loader.load();
        mainAnchor.getChildren().setAll(root);
    }

    private void accessCheck(){
        if(!accessController.checkAdminOrOperatorAccess()){
            editButton.setDisable(true);
            deleteButton.setDisable(true);
        }
    }

}

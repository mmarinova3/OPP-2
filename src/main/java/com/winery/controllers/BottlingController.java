package com.winery.controllers;

import com.winery.accessControl.AccessController;
import com.winery.entities.*;
import com.winery.notification.CriticSituations;
import com.winery.service.*;
import com.winery.utils.Connection;
import com.winery.utils.MessageBox;
import com.winery.utils.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BottlingController {
    @FXML
    private Label eventMessage;
    @FXML
    private TableView<BottledWine>bottledWineTableView;
    @FXML
    private TableColumn<BottledWine, String> wineNameColumn;
    @FXML
    private TableColumn<BottledWine, String> bottleTypeColumn;
    @FXML
    private TableColumn<BottledWine, Integer> usedBottlesNumColumn;
    @FXML
    private TableColumn<BottledWine, Date> dateColumn;
    @FXML
    private ComboBox<String> wineName;
    @FXML
    private ComboBox<Double> bottleVolume;
    @FXML
    private TextField quantityBottled;
    @FXML
    private DatePicker bottlingDate;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    private final AccessController accessController;
    private final BottledWineService bottledWineService;
    private final WineCompositionService wineCompositionService;
    private final BottleService bottleService;

    public BottlingController() {
        this.bottledWineService = BottledWineService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.wineCompositionService = WineCompositionService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.bottleService = BottleService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.accessController = new AccessController(Session.getInstance().getUser());
    }

    @FXML
    public void initialize() {
        CriticSituations.notifyPane();
        setComboBoxWine();
        setComboBoxBottle();
        accessCheck();

        wineNameColumn.setCellValueFactory(cellData -> {
            WineComposition wineComposition = cellData.getValue().getWineComposition();
            return new SimpleStringProperty(wineComposition != null ? wineComposition.getWineName() : "");
        });

        bottleTypeColumn.setCellValueFactory(cellData -> {
            Bottle bottle = cellData.getValue().getBottle();
            return new SimpleStringProperty(bottle != null ?bottle.getVolume().toString() : "");
        });

       usedBottlesNumColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
       dateColumn.setCellValueFactory(new PropertyValueFactory<>("bottlingDate"));

        List<BottledWine> bottledWines = bottledWineService.getAll();
        bottledWineTableView.getItems().addAll(bottledWines);

        bottledWineTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFieldsWithSelectedRow(newValue);
            } else {
                clearInputFields();
            }
        });
    }
    private void updateFieldsWithSelectedRow(BottledWine selectedBottledWine) {
        WineComposition wineComposition = selectedBottledWine.getWineComposition();
        wineName.setValue(wineComposition.getWineName());
        Bottle bottle= selectedBottledWine.getBottle();
        bottleVolume.setValue(bottle.getVolume());
        quantityBottled.setText(String.valueOf(selectedBottledWine.getQuantity()));
        bottlingDate.setValue(selectedBottledWine.getBottlingDate().toLocalDate());

    }

    private void clearInputFields() {
        wineName.getSelectionModel().clearSelection();
        bottleVolume.getSelectionModel().clearSelection();
        quantityBottled.clear();
        bottlingDate.setValue(null);
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
        String quantityText = quantityBottled.getText();

        if (wine == null || volume == null || quantityText.isEmpty()) {
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            eventMessage.setText("Quantity must be a valid number");
            return;
        }

        Date date = Date.valueOf(bottlingDate.getValue());


        WineComposition wineComposition = new WineComposition();
        wineComposition.setWineName(wine);
        Integer wineId = wineCompositionService.findIdByName(wineComposition.getWineName());
        wineComposition.setId(wineId);

        Bottle bottle = new Bottle();
        bottle.setVolume(volume);
        Integer bottleId = bottleService.findIdByVolume(bottle.getVolume());
        bottle.setId(bottleId);
        bottle.setQuantity(bottleService.getQuantityInStockById(bottleId));

        OptimalBottling optimalBottling = new OptimalBottling();
        int bottlesToFill = optimalBottling.getOptimalBottling(wineComposition, bottle);
        if (bottlesToFill<=0){
            eventMessage.setText("Not enough bottles");
            return;
        }

        double maxBottleInLitters= bottle.getQuantity()*bottle.getVolume();
        System.out.println("Max Bottle in Liters: " + maxBottleInLitters);


        boolean userChoice = MessageBox.showConfirmation(
                "Optimal bottling",
                "Optimal bottles (volume: " + bottle.getVolume() + ") to fill are " + bottlesToFill,
                "Do you want to bottle " + bottlesToFill + " bottles?(if you click no, it will use the value which you choose)"
        );

        int newQuantity;
        if (userChoice) {
           newQuantity=bottlesToFill;
        } else{newQuantity=quantity;}

        if(maxBottleInLitters>optimalBottling.getMaxOfWineComposition(wineComposition)){
            eventMessage.setText("Not enough wine");
            return;
        }

        if (newQuantity>bottlesToFill){
            eventMessage.setText("Not enough bottles");
            return;
        }


        BottledWine bottledWine = new BottledWine();
        bottledWine.setWineComposition(wineComposition);
        bottledWine.setBottle(bottle);
        bottledWine.setQuantity(newQuantity);
        bottledWine.setBottlingDate(date);
        clearInputFields();
        eventMessage.setText("Bottling added successfully");
        optimalBottling.updateGrapeQuantity(wineComposition,bottle.getVolume()*bottledWine.getQuantity());
        bottleService.getAndUpdateQuantityInStockById(bottleId,bottledWine.getQuantity());
        bottledWineService.save(bottledWine);
        bottledWineTableView.getItems().add(bottledWine);
        bottledWineTableView.refresh();
    }

    @FXML
    private void deleteSelectedBottledWine() {

      BottledWine selectedBottledWine=bottledWineTableView.getSelectionModel().getSelectedItem();

        if (selectedBottledWine != null) {
            bottledWineService.delete(selectedBottledWine);
            bottledWineTableView.getItems().remove(selectedBottledWine);
            bottledWineTableView.refresh();
            eventMessage.setText("Successfully deleted");
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
    /*
    @FXML
    private void bottlingBut(){
        String wine = wineName.getValue();
        Double volume = bottleVolume.getValue();

        WineComposition wineComposition = new WineComposition();
        wineComposition.setWineName(wine);
        Integer wineId = wineCompositionService.findIdByName(wineComposition.getWineName());
        wineComposition.setId(wineId);

        Bottle bottle = new Bottle();
        bottle.setVolume(volume);
        Integer bottleId = bottleService.findIdByVolume(bottle.getVolume());
        bottle.setId(bottleId);
        bottle.setQuantity(bottleService.getQuantityInStockById(bottleId));


        OptimalBottling ob = new OptimalBottling();
        ob.getOptimalBottling(wineComposition,bottle);

    }


     */
}


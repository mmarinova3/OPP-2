package com.winery.controllers;

import com.winery.entities.WineComposition;
import com.winery.service.WineCompositionService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CompositionRegisterController {

    @FXML
    private TextField newCompositionName;

    private WineCompositionService wineCompositionService;

    public CompositionRegisterController() {
        this.wineCompositionService = WineCompositionService.getInstance(Connection.getEntityManager(), Session.getInstance());
    }

    @FXML
    public void registerComposition(){
       String compositionName= newCompositionName.getText();

       if (compositionName==null){
           return;
       }

       WineComposition wineComposition = new WineComposition();
       wineComposition.setWineName(compositionName);
       wineCompositionService.save(wineComposition);
    }
}

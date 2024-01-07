package com.winery.controllers;

import com.winery.accessControl.AccessController;
import com.winery.entities.User;
import com.winery.notification.CriticSituations;
import com.winery.utils.MessageBox;
import com.winery.utils.SceneNavigator;
import com.winery.utils.Session;
import com.winery.winery_prod.Main;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class HomeController {
    @FXML
    private Label userLabel;

    @FXML
    private Label roleLabel;
    @FXML
    private Text welcomeText;

    @FXML
    private AnchorPane mainAnchor;

    private final Session session = Session.getInstance();

    private final AccessController accessController;


    public HomeController() {
        this.accessController = new AccessController(session.getUser());
    }

    @FXML
    private void initialize() {
        CriticSituations.notifyPane();

        System.out.println("UserController initialize method called");
        User currentUser = session.getUser();
        System.out.println("Welcome, " + currentUser.getUsername() + "\nRole: " + currentUser.getRoleName());

        userLabel.setText("Welcome, " + currentUser.getUsername());
        roleLabel.setText("Role: " + currentUser.getRoleName());
        welcomeText.setText("Welcome, " + currentUser.getUsername()+"!");

    }

    @FXML
    public void LogOut() throws IOException {
      Main app= new Main();
      app.changeScene("login-view.fxml",506, 312,false);

    }

    @FXML
    private void openCreateUserScene() {
        if(!accessController.checkAdminAccess()) {
           MessageBox.showMessage("UnauthorizedAccess","Insufficient privileges for the operation");
        }else{
            SceneNavigator.navigateTo("/com/winery/winery_prod/input-fxml/create-user.fxml", mainAnchor);
        }
    }
    @FXML
    private void openGrapeRegisterScene() {
        SceneNavigator.navigateTo("/com/winery/winery_prod/input-fxml/grape.fxml", mainAnchor);
    }
    @FXML
    private void openDefineGrapeScene() {
        SceneNavigator.navigateTo("/com/winery/winery_prod/input-fxml/define-grape.fxml", mainAnchor);
    }
    @FXML
    private void openRegisterBottleScene() {
        SceneNavigator.navigateTo("/com/winery/winery_prod/input-fxml/register-bottle.fxml", mainAnchor);
    }
    @FXML
    private void registerWineComposition() {
        SceneNavigator.navigateTo("/com/winery/winery_prod/input-fxml/register-composition.fxml", mainAnchor);
    }
    @FXML
    public void defineWineComponents() {
        SceneNavigator.navigateTo("/com/winery/winery_prod/input-fxml/define-components.fxml", mainAnchor);
    }
    @FXML
    private void bottleWine() {
        SceneNavigator.navigateTo("/com/winery/winery_prod/input-fxml/bottling.fxml", mainAnchor);
    }
    @FXML
    private void referencesToGrapeVarieties() {
        SceneNavigator.navigateTo("/com/winery/winery_prod/reference-fxml/grapeVarietyRef.fxml", mainAnchor);
    }
    @FXML
    private void referencesForTypesOfBottles() {
        SceneNavigator.navigateTo("/com/winery/winery_prod/reference-fxml/bottleTypeRef.fxml", mainAnchor);
    }
    @FXML
    private void referencesForTypesOfBottledWines() {
        SceneNavigator.navigateTo("/com/winery/winery_prod/reference-fxml/bottledWineRef.fxml", mainAnchor);
    }
    @FXML
    private void notificationCheck() {
        SceneNavigator.navigateTo("/com/winery/winery_prod/notifications-view.fxml", mainAnchor);
    }



}

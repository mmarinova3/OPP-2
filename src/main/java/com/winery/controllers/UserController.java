package com.winery.controllers;

import com.winery.entities.User;
import com.winery.utils.Session;
import com.winery.winery_prod.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class UserController {

    @FXML
    private Label userLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Button createUserButton;
    @FXML
    private TitledPane userTitledPane;
    @FXML
    private Button button;
    @FXML
    private AnchorPane mainAnchor;

    private final Session session = Session.getInstance();

    public UserController() {
    }


    @FXML
    private void initialize() {
        System.out.println("UserController initialize method called");
        User currentUser = session.getUser();
        System.out.println("Welcome, " + currentUser.getUsername() + "\nRole: " + currentUser.getRole());

        userLabel.setText("Welcome, " + currentUser.getUsername());
        roleLabel.setText("Role: " + currentUser.getRole());


        userTitledPane.setOnMouseEntered(event -> {
            Node titleNode = userTitledPane.lookup(".title");
            if (titleNode instanceof Label) {
                titleNode.setStyle("-fx-background-color: purple;");
            }
        });

        userTitledPane.setOnMouseExited(event -> {
            Node titleNode = userTitledPane.lookup(".title");
            if (titleNode instanceof Label) {
                titleNode.setStyle("-fx-background-color: transparent;");
            }
        });
        createUserButton.setOnMouseEntered(event -> createUserButton.setStyle("-fx-background-color: purple;"));
        createUserButton.setOnMouseExited(event -> createUserButton.setStyle("-fx-background-color: transparent;"));
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: purple;"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: transparent;"));
        /*createUserButton.setOnAction(event -> {
            try {
                openCreateUserScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/
    }

    @FXML
    public void LogOut() throws IOException {
        Main app = new Main();
        app.changeScene("login-view.fxml");
    }

    @FXML
    private void openCreateUserScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/winery/winery_prod/input-fxml/create-user.fxml"));
        Parent root = loader.load();
        mainAnchor.getChildren().setAll(root);
    }
    @FXML
    private void openGrapeRegisterScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/winery/winery_prod/input-fxml/grape.fxml"));
        Parent root = loader.load();
        mainAnchor.getChildren().setAll(root);
    }



}

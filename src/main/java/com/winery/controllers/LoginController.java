package com.winery.controllers;

import com.winery.entities.User;
import com.winery.service.UserService;
import com.winery.utils.Session;
import com.winery.winery_prod.Main;
import com.winery.utils.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

        @FXML
        public Button logIn;
        @FXML
        private Label wrongLogIn;
        @FXML
        private TextField username;
        @FXML
        private PasswordField password;



        private final UserService userService;

        public LoginController() {
                this.userService = UserService.getInstance(Connection.getEntityManager(), Session.getInstance());
        }

        @FXML
        public void initialize() {
                Session.getInstance();
        }

        @FXML
        public void userLogin() throws IOException {
                checkLogin();
        }

        private void checkLogin() throws IOException {
                Main app = new Main();

                String enteredUsername = username.getText();
                String enteredPassword = password.getText();

                if (!enteredUsername.isEmpty() && !enteredPassword.isEmpty()) {
                        User currentUser = userService.validateAndGetUser(enteredUsername, enteredPassword);

                        if (currentUser != null) {
                                wrongLogIn.setText("Successful");
                                app.changeScene("main-view.fxml");

                        } else {
                                wrongLogIn.setText("Invalid username/password");
                        }
                } else {
                        wrongLogIn.setText("Please enter a username/password");
                }
        }



}

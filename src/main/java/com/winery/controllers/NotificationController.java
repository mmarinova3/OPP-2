package com.winery.controllers;

import com.winery.notification.CriticSituations;
import com.winery.notification.Notification;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class NotificationController {

    @FXML
    private ListView<String> notificationsListView;

    private final CriticSituations criticSituations;

    public NotificationController() {
        this.criticSituations = new CriticSituations();
    }

    @FXML
    private void initialize() {
        checkGrapeVariety();
        checkBottle();
        displayNotifications(criticSituations.getNotifications());
    }


    private void checkGrapeVariety() {
        criticSituations.checkGrapeVariety();
    }

    private void checkBottle() {
        criticSituations.checkBottle();
    }

    private void displayNotifications(List<Notification> notifications) {
        for (Notification notification : notifications) {
            String notificationText = notification.getMessage();
            Label notificationLabel = new Label(notificationText);
            notificationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            notificationLabel.setTextFill(Color.RED);

            notificationsListView.getItems().add(notificationText);
        }
    }
}

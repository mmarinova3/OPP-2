package com.winery.notification;

import com.winery.entities.Bottle;
import com.winery.entities.GrapeVariety;
import com.winery.service.BottleService;
import com.winery.service.GrapeVarietyService;
import com.winery.utils.Connection;
import com.winery.utils.Session;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

import java.util.ArrayList;
import java.util.List;

public class CriticSituations {

    private final static Logger log = LogManager.getLogger(CriticSituations.class);

    private final GrapeVarietyService grapeVarietyService;
    private final BottleService bottleService;
    private final List<Notification> notifications;

    public CriticSituations() {
        this.grapeVarietyService = GrapeVarietyService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.bottleService = BottleService.getInstance(Connection.getEntityManager(), Session.getInstance());
        this.notifications = new ArrayList<>();
    }

    public void checkGrapeVariety() {
        try {
            List<GrapeVariety> grapeVarieties = grapeVarietyService.getAll();

            for (GrapeVariety grapeVariety : grapeVarieties) {
                double grapeQuantity = grapeVariety.getQuantity();

                if (grapeQuantity < 300) {
                    String notificationText = "Low quantity for grape variety " + grapeVariety.getGrapeName() + ": " + grapeQuantity;
                    log.info(notificationText);
                    Notification notification = new Notification(notificationText);
                    notifications.add(notification);
                }
            }
        } catch (Exception e) {
            log.error("Error checking grape varieties: " + e.getMessage(), e);
        }
    }

    public void checkBottle() {
        try {
            List<Bottle> bottles = bottleService.getAll();

            for (Bottle bottle : bottles) {
                double bottleQuantity = bottle.getQuantity();

                if (bottleQuantity < 50) {
                    String notificationText = "Low quantity for bottle " + bottle.getVolume() + ": " + bottleQuantity;
                    log.info(notificationText);
                    Notification notification = new Notification(notificationText);
                    notifications.add(notification);
                }
            }
        } catch (Exception e) {
            log.error("Error checking bottles: " + e.getMessage(), e);
        }
    }


    public List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    @FXML
    public static void notifyPane() {
        try {
            CriticSituations criticSituations = new CriticSituations();
            criticSituations.checkGrapeVariety();
            criticSituations.checkBottle();

            List<Notification> notifications = criticSituations.getNotifications();

            for (Notification notification : notifications) {
                Notifications notificationBuilder = Notifications.create()
                        .title("Critic Situations")
                        .text(notification.getMessage())
                        .graphic(null)
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.BOTTOM_RIGHT)
                        .onAction(event1 -> System.out.println("Notification"));

                notificationBuilder.show();
            }
        } catch (Exception e) {
            log.error("Error notifying pane: " + e.getMessage(), e);
        }
    }
}

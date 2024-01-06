package com.winery.utils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SceneNavigator {

    private final static Logger log = LogManager.getLogger(SceneNavigator.class);

    public static void navigateTo(String fxmlFileName, AnchorPane mainAnchor) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlFileName));
            Parent root = loader.load();
            mainAnchor.getChildren().setAll(root);

            log.info("Navigated to scene: {}", fxmlFileName);
        } catch (IOException e) {
            log.error("Error navigating to scene {}: {}", fxmlFileName, e.getMessage(), e);
        }
    }
}

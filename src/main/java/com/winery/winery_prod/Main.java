package com.winery.winery_prod;

import com.winery.utils.Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Main extends Application {

    private static final Logger log = LogManager.getLogger(Main.class);
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/winery/winery_prod/login-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 506, 312);
        primaryStage.setTitle("Winery Production");
        primaryStage.setResizable(true);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void changeScene(String fxml, double width, double height, boolean resizable) throws IOException {
        log.info("Changing scene to: {}", fxml);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent pane = loader.load();
        Scene scene = new Scene(pane, width, height);
        stage.setScene(scene);
        stage.setResizable(resizable);
        log.info("Scene changed successfully.");
    }

    @Override
    public void stop() {
        try {
            log.info("Application stopping...");
        } catch (Exception e) {
            log.error("Error during application stop: {}", e.getMessage(), e);
        } finally {
            Connection.closeEMF();
            log.info("Entity Manager Factory closed.");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

}

package com.winery.winery_prod;

import com.winery.entities.User;
import com.winery.utils.Connection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {


    public static void main(String[] args) {
        // Launch JavaFX application
        launch(args);

        // This code will not execute until the JavaFX application is closed

        // Create and commit a transaction (this part may not be executed immediately)
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();
            // Perform your insert operations here
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    private static Stage stage;

    @Override
    public void start(Stage primeryStage) throws IOException {
        stage=primeryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        primeryStage.setTitle("Winery Production ");
        primeryStage.setScene(scene);
        primeryStage.show();
    }

    public void changeScene(String fxml) throws IOException {
        System.out.println("Changing scene to: " + fxml);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent pane = loader.load();
        stage.getScene().setRoot(pane);
        System.out.println("Scene changed successfully.");
    }




}
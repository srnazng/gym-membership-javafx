package com.example.project3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The GymManagerMain class sets up and launches the application
 * @author Jackson Lee, Serena Zeng
 */
public class GymManagerMain extends Application {

    /**
     * Sets up stage when program starts
     * @param stage top level container for program
     * @throws IOException  exception for fxmlLoader.load()
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GymManagerMain.class.getResource("GymManagerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Project 3 - Gym Manager");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launch application
     * @param args  default arguments
     */
    public static void main(String[] args) {
        launch();
    }
}
package com.fintrack.app;

import com.fintrack.controller.FinTracker;
import com.fintrack.dao.TransactionDAO;
import com.fintrack.ui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * JavaFX entry point of FinTrack. Wires the application together:
 * database access ({@link TransactionDAO}), business logic ({@link FinTracker})
 * and the main window.
 */
public class FinApp extends Application {

    /**
     * Builds the object graph, loads the main window and shows it.
     *
     * @param stage the primary window provided by JavaFX
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FinTracker finTracker = new FinTracker(new TransactionDAO());

        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(FinApp.class.getResource("/com/fintrack/view/main-view.fxml")));
        loader.setControllerFactory(type -> new MainController(finTracker));

        Scene scene = new Scene(loader.load(), 920, 620);
        scene.getStylesheets().add(
                Objects.requireNonNull(FinApp.class.getResource("/com/fintrack/css/style.css")).toExternalForm());

        stage.setTitle("FinTrack");
        stage.setMinWidth(780);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Starts the JavaFX runtime.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        launch(args);
    }
}

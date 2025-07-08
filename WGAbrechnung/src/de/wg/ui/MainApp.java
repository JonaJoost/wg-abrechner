package de.wg.ui; 

import javafx.application.Application;
import javafx.fxml.FXMLLoader; // Diese Zeile hinzufügen
import javafx.scene.Parent;    // Diese Zeile hinzufügen
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException; // Diese Zeile hinzufügen

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Lade die FXML-Datei
            // Die .fxml-Datei muss sich im selben Verzeichnis (oder Unterverzeichnis) wie die Controller-Klasse befinden,
            // oder Sie müssen den vollen Pfad relativ zum Classpath angeben.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();

            // Erstelle eine Szene mit der geladenen Oberfläche
            Scene scene = new Scene(root, 800, 600); // Angepasste Größe für mehr Inhalt

            primaryStage.setTitle("WG-Verwaltung (JavaFX)");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Fehler beim Laden der FXML-Datei ausgeben
            System.err.println("Fehler beim Laden der MainView.fxml: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
package de.wg.ui;

import de.wg.service.Ledger;
import de.wg.service.MemberManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Ledger ledger;
    private MemberManager memberManager;

    @Override
    public void init() throws Exception {
        super.init();
        // Hier können Sie die Manager laden oder neu initialisieren
        try {
            memberManager = MemberManager.loadFromFile("members.ser");
            ledger = Ledger.loadFromFile("ledger.ser");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Fehler beim Laden der Daten: " + e.getMessage());
            memberManager = new MemberManager();
            ledger = new Ledger();
            System.out.println("Neue MemberManager und Ledger Instanzen erstellt.");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();

            // Controller-Instanz abrufen und Manager setzen
            MainController controller = loader.getController();
            if (controller != null) {
                controller.setManagers(ledger, memberManager);
                System.out.println("MainApp: setManagers wurde aufgerufen.");
            } else {
                System.err.println("MainApp: MainController konnte nicht geladen werden.");
            }

            Scene scene = new Scene(root, 800, 600);

            primaryStage.setTitle("WG-Verwaltung (JavaFX)");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Fehler beim Laden der MainView.fxml: " + e.getMessage());
            // Optional: Eine Alert-Box anzeigen, um dem Benutzer den Fehler mitzuteilen
            // Alert alert = new Alert(Alert.AlertType.ERROR);
            // alert.setTitle("Fehler");
            // alert.setHeaderText("Anwendungsfehler");
            // alert.setContentText("Die Anwendung konnte nicht gestartet werden: " + e.getMessage());
            // alert.showAndWait();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // Daten beim Beenden der Anwendung automatisch speichern
        try {
            if (memberManager != null) {
                memberManager.saveToFile("members.ser");
            }
            if (ledger != null) {
                ledger.saveToFile("ledger.ser");
            }
            System.out.println("Daten beim Beenden der Anwendung gespeichert.");
        } catch (IOException e) {
            System.err.println("Fehler beim automatischen Speichern der Daten: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
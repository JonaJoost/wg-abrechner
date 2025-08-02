package de.wg.ui;

import de.wg.service.*;
import de.wg.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * Einstiegspunkt der JavaFX-Anwendung.
 * Verwaltet den Lebenszyklus (Initialisierung, Start, Stop)
 * und die Datenmanager.
 *
 * @author  Jona
 * @version 1.4
 */
public class MainApp extends Application {
    /** Transaktions-Verwaltung. */
    private Ledger ledger;
    /** Mitglieder-Verwaltung. */
    private MemberManager memberManager;
    /** Benutzer-Verwaltung. */
    private UserManager userManager;

    /**
     * Initialisiert Manager durch Laden von Dateien.
     * Erstellt neue Manager, falls keine Dateien existieren.
     * Stellt sicher, dass ein Admin-Benutzer vorhanden ist.
     * @throws Exception bei Ladefehlern.
     */
    @Override
    public void init() throws Exception {
        super.init();
        try {
            memberManager = MemberManager.loadFromFile("members.ser");
            ledger = Ledger.loadFromFile("ledger.ser");
            userManager = UserManager.loadFromFile("users.ser");
        } catch (IOException | ClassNotFoundException e) {
            memberManager = new MemberManager();
            ledger = new Ledger();
            userManager = new UserManager();
        }
        User admin = new User("Administrator", "admin", hash("admin"), true);
        userManager.addIfNotExists(admin);
    }

    /**
     * Baut und startet die Haupt-UI der Anwendung.
     * Lädt MainView.fxml, initialisiert den Controller und zeigt das Fenster an.
     * @param primaryStage Die Hauptbühne (Stage) von JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            MainController controller = loader.getController();
            TabPane tabPane = (TabPane) root.lookup(".tab-pane"); // TabPane für den Controller finden
            controller.setManagers(ledger, memberManager, userManager, tabPane);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("WG-Verwaltung (JavaFX)");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Speichert alle Daten beim Beenden der Anwendung.
     * @throws Exception bei Speicherfehlern.
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        memberManager.saveToFile("members.ser");
        ledger.saveToFile("ledger.ser");
        userManager.saveToFile("users.ser");
    }

    /**
     * Private Hilfsmethode zum Hashen von Passwörtern.
     * @param  password Klartext-Passwort.
     * @return          SHA-256 Hash als Hex-String.
     */
    private String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return password;
        }
    }

    /**
     * Einstiegspunkt der Anwendung.
     * @param args Startargumente.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
package de.wg.ui;

import de.wg.service.Ledger;
import de.wg.service.MemberManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Einstiegspunkt der JavaFX-basierten WG-Verwaltungsanwendung.
 * 
 * Diese Klasse lädt beim Start die gespeicherten Daten (Mitglieder und
 * Transaktionen), zeigt das Hauptfenster (MainView.fxml) und speichert die
 * Daten automatisch beim Beenden.
 */

public class MainApp extends Application {

	/** Verwalter für alle Transaktionen. */
	private Ledger ledger;
	/** Verwalter für alle Mitglieder. */
	private MemberManager memberManager;

	/**
	 * Initialisierung vor dem Starten der JavaFX-Oberfläche. Lädt gespeicherte
	 * Daten oder erstellt neue Instanzen bei Fehlern.
	 *
	 * @throws Exception wenn ein unerwarteter Fehler auftritt
	 */
	@Override
	public void init() throws Exception {
		super.init();
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

	/**
	 * Startet die JavaFX-Oberfläche, lädt das Hauptfenster und übergibt die Manager
	 * an den Controller.
	 *
	 * @param primaryStage die Hauptbühne (Stage) der JavaFX-Anwendung
	 */
	@Override
	
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
			Parent root = loader.load();

			// Controller abrufen und Datenmanager übergeben
			MainController controller = loader.getController();
			if (controller != null) {
				controller.setManagers(ledger, memberManager);
				System.out.println("MainApp: setManagers wurde aufgerufen.");
			} else {
				System.err.println("MainApp: MainController konnte nicht geladen werden.");
			}

			// JavaFX-Szene aufbauen
			Scene scene = new Scene(root, 800, 600);

			primaryStage.setTitle("WG-Verwaltung (JavaFX)");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Fehler beim Laden der MainView.fxml: " + e.getMessage());
		}
	}

	/**
	 * Speichert beim Beenden der Anwendung automatisch alle Daten (Mitglieder und
	 * Transaktionen).
	 *
	 * @throws Exception wenn beim Speichern ein Fehler auftritt
	 */
	@Override
	public void stop() throws Exception {
		super.stop();
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

	/**
	 * Hauptmethode zum Start der Anwendung.
	 *
	 * @param args Kommandozeilenargumente (nicht verwendet)
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
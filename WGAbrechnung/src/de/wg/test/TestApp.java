package de.wg.test;

import de.wg.model.*;
import de.wg.service.*; 
import de.wg.exception.*; 

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Testanwendung zur Demonstration der WG-Verwaltungsfunktionen.
 * 
 * Lädt persistierte Daten (Mitglieder und Transaktionen), erstellt bei Bedarf
 * Beispieldaten und zeigt alle relevanten Informationen wie Mitgliederliste,
 * Transaktionen und Salden an. Am Ende speichert sie den Zustand wieder in
 * Dateien.
 */
public class TestApp {

	/** Dateiname zur Speicherung der Mitgliederdaten. */
	private static final String MEMBERS_FILE = "members.dat";
	/** Dateiname zur Speicherung der Transaktionsdaten. */
	private static final String LEDGER_FILE = "ledger.dat";

	/**
	 * Einstiegspunkt der Anwendung. Führt alle Testaktionen durch: Laden, Erzeugen,
	 * Anzeigen und Speichern von Mitgliedern und Transaktionen.
	 *
	 * @param args Programmargumente (nicht verwendet)
	 */
	public static void main(String[] args) {

		// 1. Lade den MemberManager oder erstelle einen neuen, falls Datei nicht
		// existiert
		MemberManager memberManager;
		try {
			memberManager = MemberManager.loadFromFile(MEMBERS_FILE);
			System.out.println("Geladene Mitglieder: " + memberManager.getAllMembers().size());
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Fehler beim Laden der Mitglieder: " + e.getMessage());
			memberManager = new MemberManager();
			System.out.println("Neuer MemberManager erstellt.");
		}

		// 2. Lade das Ledger oder erstelle ein neues
		Ledger ledger;
		try {
			ledger = Ledger.loadFromFile(LEDGER_FILE);
			System.out.println("Geladene Transaktionen: " + ledger.getAllTransactions().size());
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Fehler beim Laden des Ledgers: " + e.getMessage());
			ledger = new Ledger();
			System.out.println("Neues Ledger erstellt.");
		}

		// 3. Beispiel-Daten nur hinzufügen, wenn beides leer ist
		if (memberManager.getAllMembers().isEmpty() && ledger.getAllTransactions().isEmpty()) {
			System.out.println("\nErstelle Beispieldaten...");
			Member anna = new Member("Anna");
			Member tom = new Member("Tom");
			Member lisa = new Member("Lisa");

			// Mitglieder erstellen
			memberManager.addMember(anna);
			memberManager.addMember(tom);
			memberManager.addMember(lisa);

			// Beispiel-Transaktionen erstellen und hinzufügen
			try {
				Transaction einkauf = new Transaction(LocalDate.now(), 30.0, tom, Arrays.asList(tom, lisa),
						"Wocheneinkauf");
				ledger.addTransaction(einkauf);

				Transaction miete = new Transaction(LocalDate.now().minusDays(5), 500.0, anna,
						Arrays.asList(anna, tom, lisa), "Miete April");
				ledger.addTransaction(miete);

			} catch (UngueltigerBetragException e) {
				System.err.println("Fehler beim Hinzuf�gen der Transaktion: " + e.getMessage());
			}
		}

		// 4. Ausgabe der Mitgliederliste
		System.out.println("\n--- Mitgliederliste ---");
		for (Member m : memberManager.getAllMembers()) {
			System.out.println("Mitglied: " + m.getName());
		}

		// 5. Ausgabe der Transaktionshistorie
		System.out.println("\n--- Transaktionshistorie ---");
		for (Transaction t : ledger.getAllTransactions()) {
			System.out.println("Datum: " + t.getDate() + ", Betrag: " + t.getAmount() + ", Zahler: "
					+ t.getPayer().getName() + ", Beschreibung: " + t.getDescription());
		}

		// 6. Ausgabe der Salden
		System.out.println("\n--- Salden ---");
		ledger.printAllBalances(memberManager.getAllMembers());

		// 7. Speichern der aktuellen Daten in Dateien
		try {
			memberManager.saveToFile(MEMBERS_FILE);
			ledger.saveToFile(LEDGER_FILE);
		} catch (IOException e) {
			System.err.println("Fehler beim Speichern der Daten: " + e.getMessage());
		}
		// 8. Abschluss
		System.out.println("\nProgramm beendet.");
		System.out.println("F�hren Sie das Programm erneut aus, um zu sehen, wie die Daten geladen werden.");
	}
}
package de.wg.test;

import de.wg.model.*;
import de.wg.service.*; // Importieren Sie MemberManager und Ledger
import de.wg.exception.*; // Importieren Sie auch UngueltigerBetragException

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TestApp_persistenz {

    private static final String MEMBERS_FILE = "members.dat"; // Dateiname für Mitgliederdaten
    private static final String LEDGER_FILE = "ledger.dat";   // Dateiname für Transaktionsdaten

    public static void main(String[] args) {

        // 1. MemberManager laden oder neu erstellen
        MemberManager memberManager;
        try {
            memberManager = MemberManager.loadFromFile(MEMBERS_FILE);
            System.out.println("Geladene Mitglieder: " + memberManager.getAllMembers().size());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Fehler beim Laden der Mitglieder: " + e.getMessage());
            memberManager = new MemberManager();
            System.out.println("Neuer MemberManager erstellt.");
        }

        // 2. Ledger laden oder neu erstellen
        Ledger ledger;
        try {
            ledger = Ledger.loadFromFile(LEDGER_FILE);
            System.out.println("Geladene Transaktionen: " + ledger.getAllTransactions().size());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Fehler beim Laden des Ledgers: " + e.getMessage());
            ledger = new Ledger();
            System.out.println("Neues Ledger erstellt.");
        }

        // Füge nur Daten hinzu, wenn der Manager und das Ledger neu sind (oder leer geladen wurden)
        if (memberManager.getAllMembers().isEmpty() && ledger.getAllTransactions().isEmpty()) {
            System.out.println("\nErstelle Beispieldaten...");
            Member anna = new Member("Anna");
            Member tom = new Member("Tom");
            Member lisa = new Member("Lisa");

            memberManager.addMember(anna);
            memberManager.addMember(tom);
            memberManager.addMember(lisa);

            try {
                Transaction einkauf = new Transaction(
                        LocalDate.now(),
                        30.0,
                        tom,
                        Arrays.asList(tom, lisa),
                        "Wocheneinkauf"
                );
                ledger.addTransaction(einkauf);

                Transaction miete = new Transaction(
                        LocalDate.now().minusDays(5),
                        500.0,
                        anna,
                        Arrays.asList(anna, tom, lisa),
                        "Miete April"
                );
                ledger.addTransaction(miete);

            } catch (UngueltigerBetragException e) {
                System.err.println("Fehler beim Hinzufügen der Transaktion: " + e.getMessage());
            }
        }


        // Anzeige der aktuellen Mitglieder
        System.out.println("\n--- Mitgliederliste ---");
        for (Member m : memberManager.getAllMembers()) {
            System.out.println("Mitglied: " + m.getName());
        }

        // Anzeige der Transaktionen
        System.out.println("\n--- Transaktionshistorie ---");
        for (Transaction t : ledger.getAllTransactions()) {
            System.out.println("Datum: " + t.getDate() + ", Betrag: " + t.getAmount() + ", Zahler: " + t.getPayer().getName() + ", Beschreibung: " + t.getDescription());
        }

        // Salden anzeigen (optional, nutzt die Ledger-Funktion)
        System.out.println("\n--- Salden ---");
        ledger.printAllBalances(memberManager.getAllMembers());


        // Daten speichern, bevor das Programm beendet wird
        try {
            memberManager.saveToFile(MEMBERS_FILE);
            ledger.saveToFile(LEDGER_FILE);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Daten: " + e.getMessage());
        }

        System.out.println("\nProgramm beendet.");
        System.out.println("Führen Sie das Programm erneut aus, um zu sehen, wie die Daten geladen werden.");
    }
}
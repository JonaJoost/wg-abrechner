package de.wg.test;

import de.wg.model.Member;
import de.wg.model.Transaction;
import de.wg.service.Ledger;
import de.wg.service.MemberManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Testet die Persistenz Phase 3.
 * @author Jona
 * @version 1.1
 */
public class TestPhase3 {

    /** Test-Dateiname für Mitglieder. */
    private static final String TEST_MEMBERS_FILE = "test_members.dat";
    /** Test-Dateiname für Transaktionen. */
    private static final String TEST_LEDGER_FILE = "test_ledger.dat";

    /**
     * Hauptmethode für den Testablauf.
     * @param args Kommandozeilenargumente (nicht verwendet).
     */
    public static void main(String[] args) {
        System.out.println("Starte Tests für Phase 3");

        // 1. Persistenz (Serialisierung) testen
        System.out.println("\n1. Test der Persistenz (Speichern & Laden)");
        
        // Schritt A: Manager mit Daten erstellen und speichern
        MemberManager initialMemberManager = new MemberManager();
        Ledger initialLedger = new Ledger();

        Member jona = new Member("Jona");
        Member katha = new Member("Katha");
        initialMemberManager.addMember(jona);
        initialMemberManager.addMember(katha);

        try {
            Transaction einkauf = new Transaction(LocalDate.now(), 25.50, katha, Arrays.asList(jona, katha), "Supermarkt");
            initialLedger.addTransaction(einkauf);

            System.out.println("Speichere " + initialMemberManager.getAllMembers().size() + " Mitglieder und " + initialLedger.getAllTransactions().size() + " Transaktion(en)...");
            initialMemberManager.saveToFile(TEST_MEMBERS_FILE);
            initialLedger.saveToFile(TEST_LEDGER_FILE);

        } catch (Exception e) {
            System.err.println("Fehler beim Erstellen oder Speichern der Testdaten: " + e.getMessage());
            return;
        }

        // Schritt B: Daten in neue, leere Manager laden
        System.out.println("\nLade Daten in neue, leere Manager-Instanzen...");
        MemberManager loadedMemberManager = null;
        Ledger loadedLedger = null;

        try {
            loadedMemberManager = MemberManager.loadFromFile(TEST_MEMBERS_FILE);
            loadedLedger = Ledger.loadFromFile(TEST_LEDGER_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Fehler beim Laden der Testdaten: " + e.getMessage());
            return;
        }

        // Schritt C: Geladene Daten überprüfen
        if (loadedMemberManager != null && loadedLedger != null) {
            System.out.println("Überprüfung erfolgreich!");
            System.out.println("Anzahl geladener Mitglieder: " + loadedMemberManager.getAllMembers().size());
            System.out.println("Geladene Mitglieder: " + loadedMemberManager.getAllMembers());
            System.out.println("Anzahl geladener Transaktionen: " + loadedLedger.getAllTransactions().size());
            System.out.println("Geladene Transaktionen: " + loadedLedger.getAllTransactions());
        }

        // Schritt D: Testdateien aufräumen
        new File(TEST_MEMBERS_FILE).delete();
        new File(TEST_LEDGER_FILE).delete();
        System.out.println("\nTestdateien wurden aufgeräumt.");

        System.out.println("\nTests für Phase 3 abgeschlossen.");
    }
}
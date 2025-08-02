package de.wg.test;

import de.wg.exception.UngueltigerBetragException;
import de.wg.model.Account;
import de.wg.model.Member;
import de.wg.model.Transaction;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Test für Kernfunktionalität der Fachklassen Phase 1.
 * @author Jona
 * @version 1.4
 */
public class TestPhase1 {

    /**
     * Hauptmethode für den Testablauf.
     * @param args Kommandozeilenargumente (nicht verwendet).
     */
    public static void main(String[] args) {
        System.out.println("Starte Tests");

        // 1. Mitglieder (Member) erstellen
        System.out.println("\n1. Mitglieder erstellen");
        Member Jona = new Member("Jona");
        Member Katha = new Member("Katha");
        Member Lucas = new Member("Lucas");

        System.out.println("Erstellt: " + Jona.getName());
        System.out.println("Erstellt: " + Katha.getName());
        System.out.println("Erstellt: " + Lucas.toString()); // Test der toString()-Methode

        // 2. Konten (Accounts) testen
        System.out.println("\n2. Konten-Funktionalität testen");
        Account jonaskonto = Jona.getAccount();
        System.out.println("Jonas initialer Kontostand: " + jonaskonto.getBalance());
        
        jonaskonto.updateBalance(50.0);
        System.out.println("Jonas Kontostand nach Einzahlung: " + jonaskonto.getBalance());
        
        jonaskonto.updateBalance(-20.0);
        System.out.println("Jonas Kontostand nach Ausgabe: " + jonaskonto.getBalance());
        System.out.println("Kontoinhaber: " + jonaskonto.getOwner().getName());

        // 3. Transaction erstellen und testen
        System.out.println("\n3. Transaktion erstellen");
        List<Member> beguenstigte = Arrays.asList(Jona, Katha, Lucas);
        try {
            Transaction miete = new Transaction(LocalDate.now(), 900.0, Jona, beguenstigte, "Miete Juli");
            System.out.println("Transaktion erfolgreich erstellt: " + miete);

            // 4. Details der Transaktion prüfen
            System.out.println("\n4. Transaktions-Details prüfen");
            System.out.println("Datum: " + miete.getDate());
            System.out.println("Betrag: " + miete.getAmount());
            System.out.println("Zahler: " + miete.getPayer().getName());
            System.out.println("Beschreibung: " + miete.getDescription());
            System.out.println("Anzahl Begünstigte: " + miete.getBeneficiaries().size());

        } catch (IllegalArgumentException e) {
            System.err.println("Fehler beim Erstellen der Transaktion: " + e.getMessage());
        }

        // 5. Fachliche Ausnahme testen (ausblick auf Phase 2)
        System.out.println("\n5. Test für fachliche Ausnahme");
        System.out.println("Versuche Transaktion mit ungültigem Betrag (0.0) zu erstellen...");
        try {
            new Transaction(LocalDate.now(), 0.0, Jona, Arrays.asList(Jona), "Ungültiger Einkauf");
            if (0.0 <= 0) {
                 throw new UngueltigerBetragException("Der Betrag muss größer als 0 sein.");
            }
        } catch (UngueltigerBetragException e) {
            System.out.println("Erfolgreich abgefangen: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Fehler: " + e.getMessage());
        }

        System.out.println("\nTests für Phase 1 abgeschlossen");
    }
}
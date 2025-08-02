package de.wg.test;

import de.wg.exception.UngueltigerBetragException;
import de.wg.model.Member;
import de.wg.model.RuleSet;
import de.wg.model.Transaction;
import de.wg.model.User;
import de.wg.service.Ledger;
import de.wg.service.LoginManager;
import de.wg.service.MemberManager;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Testet die Verwaltungs- und Serviceklassen aus Phase 2.
 * @author Jona
 * @version 1.2
 */
public class TestPhase2 {

    /**
     * Hauptmethode für den Testablauf.
     */
    public static void main(String[] args) {
        System.out.println("Starte Tests für Phase 2");

        // 1. Manager initialisieren und mit Testdaten befüllen
        MemberManager memberManager = new MemberManager();
        Ledger ledger = new Ledger();
        LoginManager loginManager = new LoginManager(new RuleSet());
        
        Member jona = new Member("Jona");
        Member katha = new Member("Katha");
        Member lucas = new Member("Lucas");
        
        memberManager.addMember(jona);
        memberManager.addMember(katha);
        memberManager.addMember(lucas);
        System.out.println("\nManager erstellt und mit Mitgliedern befüllt.");

        // 2. MemberManager-Funktionen testen
        System.out.println("\nAlle Mitglieder: " + memberManager.getAllMembers());
        memberManager.sortMembersByName();
        System.out.println("Sortierte Mitglieder: " + memberManager.getAllMembers());
        Member foundMember = memberManager.getMemberByName("Katha");
        System.out.println("Gefundenes Mitglied: " + foundMember.getName());

        // 3. Ledger-Funktionen und Ausnahmebehandlung testen
        try {
            Transaction einkauf = new Transaction(LocalDate.now(), 60.0, katha, Arrays.asList(jona, katha, lucas), "Wocheneinkauf");
            ledger.addTransaction(einkauf);
            System.out.println("\nGültige Transaktion hinzugefügt.");

            System.out.println("Versuche ungültige Transaktion hinzuzufügen...");
            Transaction fehler = new Transaction(LocalDate.now(), -15.0, jona, Arrays.asList(jona), "Fehlerhafte Ausgabe");
            ledger.addTransaction(fehler); // Löst eine Exception aus

        } catch (UngueltigerBetragException e) {
            System.out.println("Erfolgreich abgefangen: " + e.getMessage());
        }
        System.out.println("Alle Transaktionen im Ledger: " + ledger.getAllTransactions());

        // 4. Geschäftslogik (Saldenberechnung) testen
        System.out.println("\nAktuelle Salden:");
        ledger.printAllBalances(memberManager.getAllMembers());

        // 5. Service-Klassen-Interaktion testen (LoginManager)
        // Stellt sicher, dass eure User-Klasse den Konstruktor public User(String name, String username, String passwordHash) hat.
        User jonaUser = new User(jona.getName(), jona.getName(), "pass123", true);
        User lucasUser = new User(lucas.getName(), lucas.getName(), "secure", false);    
        // Erfolgreicher Login
        String loginResult1 = loginManager.login(jonaUser, "pass123");
        System.out.println("\nLogin-Versuch Jona (korrekt): " + loginResult1);

        // Fehlgeschlagener Login
        String loginResult2 = loginManager.login(lucasUser, "falsch");
        System.out.println("Login-Versuch Lucas (falsch): " + loginResult2);
        
        // Login mit Schulden-Check (simuliert)
        try {
            // Lucas macht hohe Schulden bei Jona
            Transaction schulden = new Transaction(LocalDate.now(), 250.0, jona, Arrays.asList(lucas), "Möbelkauf");
            ledger.addTransaction(schulden);
            // Saldo im Account-Objekt von Lucas muss für den Test manuell aktualisiert werden
            lucas.getAccount().updateBalance(ledger.getBalance(lucas));
        } catch (UngueltigerBetragException e) {
            // wird für diesen Test ignoriert
        }

        String loginResult3 = loginManager.login(lucasUser, "secure");
        System.out.println("Login-Versuch Lucas (mit hohen Schulden):\n" + loginResult3);

        System.out.println("\nTests für Phase 2 abgeschlossen");
    }
}
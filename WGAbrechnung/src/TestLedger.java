

import de.wg.model.*;
import de.wg.service.Ledger;
import de.wg.exception.UngueltigerBetragException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TestLedger {

    public static void main(String[] args) {

        // Mitglieder erstellen
        Member anna = new Member("Anna");
        Member tom = new Member("Tom");
        Member lisa = new Member("Lisa");

        // Ledger anlegen
        Ledger ledger = new Ledger();

        // T07: Gültige Transaktion
        try {
            Transaction einkauf = new Transaction(
                    LocalDate.now(),
                    25.0,
                    anna,
                    Arrays.asList(anna, tom),
                    "Wocheneinkauf"
            );
            ledger.addTransaction(einkauf);
            System.out.println("T07 OK: Transaktion hinzugefügt");
        } catch (UngueltigerBetragException e) {
            System.out.println("T07 FEHLER: Sollte gültig sein");
        }

        // T08: Ungültiger Betrag (0€)
        try {
            Transaction falsch = new Transaction(
                    LocalDate.now(),
                    0.0,
                    tom,
                    Arrays.asList(tom, lisa),
                    "Fehlerhafte Transaktion"
            );
            ledger.addTransaction(falsch);
            System.out.println("T08 FEHLER: Exception wurde NICHT geworfen");
        } catch (UngueltigerBetragException e) {
            System.out.println("T08 OK: Exception korrekt erkannt -> " + e.getMessage());
        }

        // T09: Saldo prüfen (Anna zahlt 30€ für Anna und Tom)
        try {
            Transaction zweite = new Transaction(
                    LocalDate.now(),
                    30.0,
                    anna,
                    Arrays.asList(anna, tom),
                    "Putzequipment"
            );
            ledger.addTransaction(zweite);
        } catch (Exception e) {
            System.out.println("T09 FEHLER bei Transaktion");
        }

        double saldoAnna = ledger.getBalance(anna);
        double saldoTom = ledger.getBalance(tom);
        System.out.println("T09: Anna: " + saldoAnna + " EUR | Tom: " + saldoTom + " EUR");

        // T10: Suche nach heutigem Datum
        List<Transaction> heute = ledger.findTransactionsByDate(LocalDate.now());
        System.out.println("T10: Gefundene Transaktionen heute: " + heute.size());

        // T11: Sortierung nach Betrag
        System.out.println("T11: Transaktionen sortiert nach Betrag:");
        List<Transaction> sortiert = ledger.getTransactionsSortedByAmount();
        for (Transaction t : sortiert) {
            System.out.println(t.getDescription() + ": " + t.getAmount() + " EUR");
        }
    }
}

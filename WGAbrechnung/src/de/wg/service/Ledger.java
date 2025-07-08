// Klasse zur Verwaltung aller Transaktionen und zur Berechnung von Schulden
package de.wg.service;

import de.wg.model.*;
import de.wg.exception.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Ledger implements Serializable {
    private static final long serialVersionUID = 1L;

    // Eine Liste, in der alle Transaktionen gespeichert werden
    private List<Transaction> transactions;

    // Konstruktor: Die Liste wird beim Erzeugen des Ledgers initialisiert
    public Ledger() {
        transactions = new ArrayList<>();
    }

    // Methode zum Hinzufügen einer Transaktion zur Liste
    public void addTransaction(Transaction t) throws UngueltigerBetragException {
        if (t == null) {
            throw new IllegalArgumentException("Transaktion darf nicht null sein");
        }
        if (t.getAmount() <= 0) {
            throw new UngueltigerBetragException("Der Betrag muss größer als 0 sein");
        }
        transactions.add(t);
    }

    // Gibt die Liste aller gespeicherten Transaktionen zurück
    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    // Berechnet den Kontostand (Saldo) eines bestimmten Mitglieds
    public double getBalance(Member member) {
        double balance = 0.0;

        for (Transaction t : transactions) {
            if (t.getPayer().equals(member)) {
                balance += t.getAmount();
            }

            if (t.getBeneficiaries().contains(member)) {
                int anzahl = t.getBeneficiaries().size();
                balance -= t.getAmount() / anzahl;
            }
        }

        return balance;
    }

    // Gibt alle Salden aller Mitglieder aus (einzeln nacheinander)
    public void printAllBalances(List<Member> mitglieder) {
        for (Member m : mitglieder) {
            double saldo = getBalance(m);
            System.out.println(m.getName() + " hat einen Saldo von " + saldo + " EUR");
        }
    }

    // Suche nach allen Transaktionen an einem bestimmten Datum
    public List<Transaction> findTransactionsByDate(LocalDate date) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getDate().equals(date)) {
                result.add(t);
            }
        }
        return result;
    }

    // Gibt die Transaktionen sortiert nach Datum zurück (älteste zuerst)
    public List<Transaction> getTransactionsSortedByDate() {
        List<Transaction> sorted = new ArrayList<>(transactions);
        Collections.sort(sorted, Comparator.comparing(Transaction::getDate));
        return sorted;
    }

    // Gibt die Transaktionen sortiert nach Betrag (höchster zuerst)
    public List<Transaction> getTransactionsSortedByAmount() {
        List<Transaction> sorted = new ArrayList<>(transactions);
        Collections.sort(sorted, (t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()));
        return sorted;
    }
}

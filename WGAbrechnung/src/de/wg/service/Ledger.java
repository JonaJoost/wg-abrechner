package de.wg.service;

import de.wg.model.*; // Stellt sicher, dass alle benötigten Model-Klassen importiert sind
import de.wg.exception.*;

import java.io.*; // Wichtig: Imports für Serialisierung und Dateihandhabung
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
        return Collections.unmodifiableList(transactions); // Sollte unveränderlich sein
    }

    // Berechnet den Kontostand (Saldo) eines bestimmten Mitglieds
    public double getBalance(Member member) {
        double balance = 0.0;

        for (Transaction t : transactions) {
            if (t.getPayer().equals(member)) {
                balance += t.getAmount(); // Zahler hat Guthaben
            }
            if (t.getBeneficiaries().contains(member)) {
                int anzahl = t.getBeneficiaries().size();
                balance -= t.getAmount() / anzahl; // Begünstigter hat Schulden
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

    /**
     * Speichert den aktuellen Zustand des Ledger-Objekts (und damit alle Transaktionen)
     * in einer Datei unter dem angegebenen Dateinamen.
     *
     * @param filename Der Pfad und Dateiname, unter dem die Daten gespeichert werden sollen.
     * @throws IOException Falls ein Fehler beim Schreiben der Datei auftritt.
     */
    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this); // Schreibt das gesamte Ledger-Objekt
            System.out.println("Ledger erfolgreich in " + filename + " gespeichert.");
        }
    }

    /**
     * Lädt ein Ledger-Objekt aus einer Datei.
     * Wenn die Datei nicht gefunden wird, wird ein neues, leeres Ledger zurückgegeben.
     *
     * @param filename Der Pfad und Dateiname, aus dem die Daten geladen werden sollen.
     * @return Das geladene Ledger-Objekt.
     * @throws IOException            Falls ein anderer Fehler beim Lesen der Datei auftritt (außer FileNotFoundException).
     * @throws ClassNotFoundException Falls die Klasse des serialisierten Objekts nicht gefunden wird.
     */
    public static Ledger loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject(); // Liest das Objekt aus der Datei
            if (obj instanceof Ledger) {
                System.out.println("Ledger erfolgreich aus " + filename + " geladen.");
                return (Ledger) obj; // Castet es zurück zum Ledger
            } else {
                throw new IOException("Datei enthält kein gültiges Ledger-Objekt.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Die Datei '" + filename + "' wurde nicht gefunden. Ein neuer Ledger wird erstellt.");
            return new Ledger(); // Wenn Datei nicht existiert, starte mit neuem Ledger
        }
    }
}
package de.wg.service;

import de.wg.model.*;
import de.wg.exception.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Die Klasse {@code Ledger} verwaltet eine Liste von Transaktionen innerhalb
 * einer Wohngemeinschaft. Sie ermöglicht das Hinzufügen, Suchen und Sortieren
 * von Transaktionen sowie das Berechnen von Salden. Zusätzlich kann das Objekt
 * serialisiert und persistiert werden.
 */

public class Ledger implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Liste aller erfassten Transaktionen. */
	private List<Transaction> transactions;

	/**
	 * Erstellt ein neues {@code Ledger}-Objekt mit einer leeren Transaktionsliste.
	 */
	public Ledger() {
		transactions = new ArrayList<>();
	}

	/**
	 * Fügt eine neue Transaktion zur Liste hinzu.
	 *
	 * @param t die hinzuzufügende Transaktion
	 * @throws IllegalArgumentException   wenn {@code t} {@code null} ist
	 * @throws UngueltigerBetragException wenn der Betrag der Transaktion kleiner
	 *                                    oder gleich null ist
	 */
	public void addTransaction(Transaction t) throws UngueltigerBetragException {
		if (t == null) {
			throw new IllegalArgumentException("Transaktion darf nicht null sein");
		}
		if (t.getAmount() <= 0) {
			throw new UngueltigerBetragException("Der Betrag muss größer als 0 sein");
		}
		transactions.add(t);
	}

	/**
	 * Gibt eine unveränderliche Liste aller gespeicherten Transaktionen zurück.
	 *
	 * @return eine Liste aller Transaktionen
	 */
	public List<Transaction> getAllTransactions() {
		return Collections.unmodifiableList(transactions); 
	}

	/**
	 * Berechnet den aktuellen Saldo eines Mitglieds basierend auf allen
	 * Transaktionen.
	 *
	 * @param member das Mitglied, dessen Saldo berechnet werden soll
	 * @return der Saldo (positiv = Guthaben, negativ = Schulden)
	 */
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

	/**
	 * Gibt die Salden aller übergebenen Mitglieder in der Konsole aus.
	 *
	 * @param mitglieder Liste der Mitglieder, deren Salden ausgegeben werden sollen
	 */
	public void printAllBalances(List<Member> mitglieder) {
		for (Member m : mitglieder) {
			double saldo = getBalance(m);
			System.out.println(m.getName() + " hat einen Saldo von " + String.format("%.2f", saldo) + " EUR");
		}
	}

	/**
	 * Sucht nach Transaktionen, die an einem bestimmten Datum durchgeführt wurden.
	 *
	 * @param date das Datum, nach dem gesucht werden soll
	 * @return Liste der Transaktionen an diesem Datum
	 */
	public List<Transaction> findTransactionsByDate(LocalDate date) {
		List<Transaction> result = new ArrayList<>();
		for (Transaction t : transactions) {
			if (t.getDate().equals(date)) {
				result.add(t);
			}
		}
		return result;
	}

	/**
	 * Gibt alle Transaktionen sortiert nach Datum zurück (älteste zuerst).
	 *
	 * @return Liste der Transaktionen, aufsteigend nach Datum sortiert
	 */
	public List<Transaction> getTransactionsSortedByDate() {
		List<Transaction> sorted = new ArrayList<>(transactions);
		Collections.sort(sorted, Comparator.comparing(Transaction::getDate));
		return sorted;
	}

	/**
	 * Gibt alle Transaktionen sortiert nach Betrag zurück (größter zuerst).
	 *
	 * @return Liste der Transaktionen, absteigend nach Betrag sortiert
	 */
	public List<Transaction> getTransactionsSortedByAmount() {
		List<Transaction> sorted = new ArrayList<>(transactions);
		Collections.sort(sorted, (t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()));
		return sorted;
	}

	/**
	 * Speichert das aktuelle {@code Ledger}-Objekt einschließlich aller
	 * Transaktionen in eine Datei.
	 *
	 * @param filename Pfad und Dateiname, unter dem gespeichert werden soll
	 * @throws IOException bei einem Fehler beim Schreiben der Datei
	 */
	public void saveToFile(String filename) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
			oos.writeObject(this); 
			System.out.println("Ledger erfolgreich in " + filename + " gespeichert.");
		}
	}

	/**
	 * Lädt ein {@code Ledger}-Objekt aus einer Datei. Falls die Datei nicht
	 * existiert, wird ein neues leeres {@code Ledger} zurückgegeben.
	 *
	 * @param filename Pfad und Dateiname, aus dem geladen werden soll
	 * @return das geladene {@code Ledger}-Objekt
	 * @throws IOException            wenn ein Fehler beim Lesen auftritt (außer
	 *                                {@code FileNotFoundException})
	 * @throws ClassNotFoundException wenn die Klasse beim Deserialisieren nicht
	 *                                gefunden wird
	 */
	public static Ledger loadFromFile(String filename) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			Object obj = ois.readObject(); 
			if (obj instanceof Ledger) {
				System.out.println("Ledger erfolgreich aus " + filename + " geladen.");
				return (Ledger) obj; 
			} else {
				throw new IOException("Datei enthält kein gültiges Ledger-Objekt.");
			}
		} catch (FileNotFoundException e) {
			System.out.println("Die Datei '" + filename + "' wurde nicht gefunden. Ein neuer Ledger wird erstellt.");
			return new Ledger(); 
		}
	}
}
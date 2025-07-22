package de.wg.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Repräsentiert eine einzelne finanzielle Transaktion innerhalb der WG. Eine
 * Transaktion erfasst Details wie Datum, Betrag, wer gezahlt hat, wer
 * Begünstigter ist und eine Beschreibung.
 * <p>
 * Die Klasse ist {@link Serializable} für die Datenpersistenz und
 * {@link Comparable} für die Standard-Sortierung nach Datum (chronologisch).
 * </p>
 * 
 * @author Jona
 * @version 1.0
 * @since 2024-07-08
 */
public class Transaction implements Serializable, Comparable<Transaction> {
	private static final long serialVersionUID = 1L;

	private LocalDate date; // Datum der Transaktion
	private double amount; // Betrag der Transaktion
	private Member payer; // Das WG-Mitglied, das die Transaktion bezahlt hat
	private List<Member> beneficiaries; // Die WG-Mitglieder, die von der Transaktion profitieren
	private String description; // Beschreibung der Transaktion
	private boolean verrechnet = false; // Status, ob die Transaktion bereits verrechnet wurde

	/**
	 * Konstruktor für eine neue Transaktion.
	 *
	 * @param date          Das Datum, an dem die Transaktion stattfand. Muss nicht
	 *                      null sein.
	 * @param amount        Der Betrag der Transaktion. Muss größer als 0 sein (wird
	 *                      im Ledger geprüft).
	 * @param payer         Das {@link Member}-Objekt, das die Ausgabe getätigt hat.
	 *                      Muss nicht null sein.
	 * @param beneficiaries Eine Liste von {@link Member}-Objekten, die von der
	 *                      Ausgabe profitieren. Darf nicht null oder leer sein.
	 * @param description   Eine kurze Beschreibung der Transaktion.
	 * @throws IllegalArgumentException Wenn Datum, Zahler, Begünstigte null sind
	 *                                  oder die Begünstigtenliste leer ist.
	 */
	public Transaction(LocalDate date, double amount, Member payer, List<Member> beneficiaries, String description) {
		if (date == null || payer == null || beneficiaries == null || beneficiaries.isEmpty()) {
			throw new IllegalArgumentException(
					"Ungültige Transaktionsdaten: Datum, Zahler und Begünstigte dürfen nicht null sein und Begünstigtenliste darf nicht leer sein.");
		}
		this.date = date;
		this.amount = amount;
		this.payer = payer;
		this.beneficiaries = beneficiaries;
		this.description = description;
	}

	/**
	 * Gibt das Datum der Transaktion zur�ck.
	 * 
	 * @return Das Datum der Transaktion.
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Gibt den Betrag der Transaktion zur�ck.
	 * 
	 * @return Der Betrag der Transaktion.
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Gibt das WG-Mitglied zurück, das die Transaktion bezahlt hat.
	 * 
	 * @return Das {@link Member}-Objekt des Zahlers.
	 */
	public Member getPayer() {
		return payer;
	}

	/**
	 * Gibt die Liste der WG-Mitglieder zurück, die von der Transaktion profitieren.
	 * 
	 * @return Eine Liste von {@link Member}-Objekten, die Begünstigten.
	 */
	public List<Member> getBeneficiaries() {
		return beneficiaries;
	}

	/**
	 * Gibt die Beschreibung der Transaktion zurück.
	 * 
	 * @return Die Beschreibung der Transaktion als String.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Prüft, ob die Transaktion bereits verrechnet wurde.
	 * 
	 * @return True, wenn die Transaktion verrechnet wurde, sonst false.
	 */
	public boolean isVerrechnet() {
		return verrechnet;
	}

	/**
	 * Setzt den Verrechnungsstatus der Transaktion.
	 * 
	 * @param verrechnet True, wenn die Transaktion als verrechnet markiert werden
	 *                   soll, sonst false.
	 */
	public void setVerrechnet(boolean verrechnet) {
		this.verrechnet = verrechnet;
	}

	/**
	 * Gibt eine String-Repräsentation der Transaktion zurück.
	 * 
	 * @return Eine formatierte String-Repräsentation der Transaktion.
	 */
	@Override
	public String toString() {
		return "Transaktion am " + date + ": " + description + " (" + amount + " EUR, gezahlt von " + payer.getName()
				+ ")";
	}

	/**
	 * Vergleicht dieses Transaktion-Objekt mit dem angegebenen Transaktion-Objekt
	 * basierend auf dem Datum. Dies ermöglicht das Sortieren von Transaktionen
	 * chronologisch.
	 *
	 * @param other Die andere Transaktion, mit der verglichen werden soll.
	 * @return Ein negativer Integer, null oder ein positiver Integer, wenn diese
	 *         Transaktion früher, am gleichen Datum oder später als die angegebene
	 *         Transaktion stattfand.
	 */
	@Override
	public int compareTo(Transaction other) {
		return this.date.compareTo(other.getDate());
	}
}
package de.wg.model;

import java.io.Serializable;

/**
 * Repräsentiert ein Konto eines WG-Mitglieds. Jedes Mitglied
 * besitzt ein solches Konto, auf welchem das aktuelle Saldo (Guthaben oder
 * Schulden) geführt wird.
 * <p>
 * Die Klasse ist {@link Serializable} für die Datenpersistenz und
 * {@link Comparable} für die Sortierung nach Kontostand (Schulden zuerst).
 * </p>
 * 
 * @author Jona
 * @version 1.1
 */
public class Account implements Serializable, Comparable<Account> {
	private static final long serialVersionUID = 1L;

	private Member owner; 
	private double balance = 0.0;

	/**
	 * Konstruktor für ein neues Konto. Ein Konto wird immer einem spezifischen
	 * WG-Mitglied zugeordnet.
	 *
	 * @param owner Das {@link Member}-Objekt, dem dieses Konto gehört.
	 */
	public Account(Member owner) {
		this.owner = owner;
	}

	/**
	 * Gibt den aktuellen Kontostand (Saldo) zurück. Ein negativer Wert bedeutet
	 * Schulden, ein positiver Wert Guthaben.
	 *
	 * @return Der aktuelle Kontostand des Mitglieds.
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * Aktualisiert den Kontostand um den angegebenen Delta-Wert. Ein positiver
	 * Delta-Wert erhöht das Guthaben, ein negativer verringert es (erhöht
	 * Schulden).
	 *
	 * @param delta Der Betrag, um den der Kontostand geändert werden soll.
	 */
	public void updateBalance(double delta) {
		balance += delta;
	}

	/**
	 * Gibt das WG-Mitglied zurück, dem dieses Konto gehört.
	 *
	 * @return Das {@link Member}-Objekt, der Besitzer des Kontos.
	 */
	public Member getOwner() {
		return owner;
	}

	/**
	 * Vergleicht dieses Account-Objekt mit dem angegebenen Account-Objekt basierend
	 * auf dem Kontostand. Konten mit höheren Schulden (kleinerer Saldo) erscheinen
	 * vor Konten mit weniger Schulden oder Guthaben.
	 *
	 * @param other Das andere Account-Objekt, mit dem verglichen werden soll.
	 * @return Ein negativer Integer, null oder ein positiver Integer, wenn dieses
	 *         Konto einen geringeren, gleichen oder höheren Saldo als das
	 *         angegebene Konto hat (Schulden zuerst).
	 */
	@Override
	public int compareTo(Account other) {
		return Double.compare(this.balance, other.getBalance());
	}
}
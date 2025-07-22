package de.wg.model;

import java.io.Serializable;

/**
 * Repräsentiert ein WG-Mitglied. Erbt von {@link Person} und implementiert
 * {@link AccountHolder}. Ein Mitglied besitzt ein eigenes
 * {@link Account}-Objekt.
 */

public class Member extends Person implements AccountHolder, Serializable {
	private static final long serialVersionUID = 1L;

	/** Das Konto des Mitglieds. */
	private Account account;

	/**
	 * Konstruktor zum Erzeugen eines neuen WG-Mitglieds mit einem Namen. Erstellt
	 * gleichzeitig ein neues Konto für das Mitglied.
	 *
	 * @param name der Name des Mitglieds
	 */

	public Member(String name) { 
		super(name); 
		this.account = new Account(this); 
	}

	/**
	 * Gibt das Konto des Mitglieds zurück.
	 *
	 * @return das {@link Account}-Objekt des Mitglieds
	 */

	public Account getAccount() {
		return account;
	}

	/**
	 * Setzt ein neues Konto für das Mitglied.
	 *
	 * @param account das neue {@link Account}-Objekt
	 */

	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * Gibt den Namen des Mitglieds zurück. Diese Methode überschreibt ggf. die
	 * Methode der Basisklasse.
	 *
	 * @return der Name des Mitglieds
	 */

	public String getName() { 
		return super.getName(); 
	}
}
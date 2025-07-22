package de.wg.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Klasse für Benutzer erbt von Person und kann sich anmelden.
 */
public class User extends Person implements Serializable {
	private static final long serialVersionUID = 1L;

	private String username; // eindeutiger Benutzername für Login
	private String passwordHash; // gespeichertes Passwort (als Hash)

	/**
	 * Konstruktor für einen Benutzer. Ruft den Konstruktor der Elternklasse Person
	 * auf.
	 * 
	 * @param name         Der vollständige Name der Person.
	 * @param username     Der eindeutige Benutzername für den Login.
	 * @param passwordHash Der gehashte Wert des Passworts.
	 */
	public User(String name, String username, String passwordHash) {
		super(name); 
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Benutzername darf nicht leer sein.");
		}
		if (passwordHash == null || passwordHash.isBlank()) {
			throw new IllegalArgumentException("Passwort-Hash darf nicht leer sein.");
		}
		this.username = username;
		this.passwordHash = passwordHash;
	}

	/**
	 * Gibt den Benutzernamen zur�ck.
	 * 
	 * @return Der Benutzername.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gibt den Passwort-Hash zur�ck (intern verwendet).
	 * 
	 * @return Der Passwort-Hash.
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * überprüft, ob der eingegebene Hash dem gespeicherten Hash entspricht.
	 * 
	 * @param inputHash Der eingegebene Passwort-Hash.
	 * @return true, wenn die Passw�rter �bereinstimmen, sonst false.
	 */
	public boolean verifyPassword(String inputHash) {
		return Objects.equals(passwordHash, inputHash);
	}

	/**
	 * Gibt eine String-Repräsentation des Benutzers zurück.
	 * 
	 * @return Benutzername und Name in Klammern.
	 */
	@Override
	public String toString() {
		return username + " (" + name + ")";
	}
}
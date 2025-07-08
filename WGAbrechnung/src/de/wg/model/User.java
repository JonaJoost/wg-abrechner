package de.wg.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Klasse f�r Benutzer erbt von Person und implementiert AccountHolder.
 * Jeder User hat nun ein zugeh�riges Konto.
 * <p>
 * Die Klasse ist {@link Serializable}, um eine sp�tere Persistenz der Objekte zu erm�glichen.
 * Sie erbt auch die {@link Comparable}-Implementierung von {@link Person} (Sortierung nach Name).
 * </p>
 * @author Jona
 * @version 1.1
 * @since 2024-07-08
 */
public class User extends Person implements AccountHolder, Serializable {
    private static final long serialVersionUID = 1L;

    private String username;       // eindeutiger Benutzername f�r Login
    private String passwordHash;   // gespeichertes Passwort (als Hash)
    private Account account;       // Zugeh�riges Konto des Benutzers

    /**
     * Konstruktor f�r einen User mit Namen, Benutzername, Passwort-Hash und einem Account.
     * @param name Der Anzeigename des Users (geerbt von Person).
     * @param username Der eindeutige Benutzername f�r den Login.
     * @param passwordHash Der gehashte String des Passworts.
     * @param account Das Account-Objekt, das diesem User zugeordnet ist. Darf nicht null sein.
     * @throws IllegalArgumentException falls der Name, Benutzername, Passwort-Hash oder Account null ist.
     */
    public User(String name, String username, String passwordHash, Account account) {
        super(name); // ruft Konstruktor von Person auf

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Benutzername darf nicht leer sein.");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Passwort-Hash darf nicht leer sein.");
        }
        if (account == null) {
            throw new IllegalArgumentException("Account darf nicht null sein.");
        }

        this.username = username;
        this.passwordHash = passwordHash;
        this.account = account;
    }

    /**
     * Getter f�r Benutzername.
     * @return Der Benutzername.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Passwort-Hash zur�ckgeben (nur intern verwendet).
     * @return Der Passwort-Hash.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * �berpr�ft, ob eingegebener Hash dem gespeicherten entspricht.
     * @param inputHash Der einzugebende Hash zum Vergleich.
     * @return true, wenn die Hashes �bereinstimmen, sonst false.
     */
    public boolean verifyPassword(String inputHash) {
        return Objects.equals(passwordHash, inputHash);
    }

    /**
     * �berschreibt die toString-Methode, um den User besser darzustellen.
     * @return Eine String-Repr�sentation des Users (Benutzername (Name)).
     */
    @Override
    public String toString() {
        return username + " (" + getName() + ")";
    }

    // --- Implementierung des AccountHolder Interfaces ---

    /**
     * Gibt das Konto des Account-Inhabers zur�ck.
     * @return Das Account-Objekt dieses Users.
     */
    @Override
    public Account getAccount() {
        return account;
    }

    /**
     * Gibt den Namen des Account-Inhabers zur�ck.
     * Diese Methode wird von der geerbten Person-Klasse bereitgestellt.
     * @return Der Name als String.
     */
    // @Override // Diese Annotation ist technisch nicht notwendig, da von Person geerbt
    // public String getName() {
    //     return super.getName();
    // }
}
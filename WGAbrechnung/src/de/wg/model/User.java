package de.wg.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Repräsentiert einen Systembenutzer mit Anmeldedaten und Berechtigungen.
 * Diese Klasse erbt von {@link Person} und fügt benutzerspezifische
 * Eigenschaften wie Benutzername, Passwort und Admin-Status hinzu.
 *
 * @author  Jona
 * @version 1.1
 */
public class User extends Person implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Der eindeutige Benutzername für den Login. */
    private String username;
    
    /** Der gespeicherte Hash des Passworts. */
    private String passwordHash;
    
    /** Gibt an, ob der Benutzer Administratorrechte besitzt. */
    private boolean isAdmin;

    /**
     * Konstruktor zum Erstellen eines neuen Benutzers.
     *
     * @param name         Der vollständige Name des Benutzers (von {@link Person}).
     * @param username     Der eindeutige Benutzername für den Login.
     * @param passwordHash Der gehashte Wert des Passworts.
     * @param isAdmin      Gibt an, ob der Benutzer Administratorrechte hat.
     */
    public User(String name, String username, String passwordHash, boolean isAdmin) {
        super(name);
        this.username = username;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
    }

    /**
     * Gibt den Benutzernamen zurück.
     *
     * @return Der Benutzername als String.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gibt den gespeicherten Passwort-Hash zurück.
     *
     * @return Der Passwort-Hash als String.
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Überprüft, ob der eingegebene Hash mit dem gespeicherten Hash übereinstimmt.
     *
     * @param  inputHash Der zu überprüfende Passwort-Hash.
     * @return           {@code true}, wenn die Passwörter übereinstimmen, andernfalls {@code false}.
     */
    public boolean verifyPassword(String inputHash) {
        return Objects.equals(passwordHash, inputHash);
    }

    /**
     * Gibt an, ob der Benutzer Administratorrechte hat.
     *
     * @return {@code true}, wenn der Benutzer ein Administrator ist, andernfalls {@code false}.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Gibt eine String-Repräsentation des Benutzers zurück.
     * Das Format ist "username (Name) [Admin]". Der Admin-Zusatz erscheint nur,
     * wenn der Benutzer Administrator ist.
     *
     * @return Eine formatierte Zeichenkette des Benutzerobjekts.
     */
    @Override
    public String toString() {
        return username + " (" + getName() + ")" + (isAdmin ? " [Admin]" : "");
    }
}
// Klasse f�r Benutzer erbt von Person
package de.wg.model;

import java.io.Serializable;
import java.util.Objects;

public class User extends Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;       // eindeutiger Benutzername f�r Login
    private String passwordHash;   // gespeichertes Passwort (als Hash)

    // Konstruktor: ruft super(name) aus Person auf
    public User(String name, String username, String passwordHash) {
        super(name);
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // Getter f�r Benutzername
    public String getUsername() {
        return username;
    }

    // Passwort-Hash zur�ckgeben (nur intern verwendet)
    public String getPasswordHash() {
        return passwordHash;
    }

    // �berpr�ft, ob eingegebener Hash dem gespeicherten entspricht
    public boolean verifyPassword(String inputHash) {
        return Objects.equals(passwordHash, inputHash);
    }

    // Benutzername als Identifikation ausgeben
    @Override
    public String toString() {
        return username + " (" + name + ")";
    }
}

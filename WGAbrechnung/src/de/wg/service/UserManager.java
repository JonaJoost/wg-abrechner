package de.wg.service;

import de.wg.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Verwaltet eine Liste von {@link User}-Objekten.
 * Bietet Methoden zum Hinzufügen, Suchen und zur Persistenz.
 *
 * @author  Jona
 * @version 1.1
 */
public class UserManager implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Liste aller verwalteten Benutzer. */
    private List<User> users = new ArrayList<>();

    /**
     * Fügt einen Benutzer hinzu.
     * @param user Benutzer-Objekt.
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Fügt Benutzer hinzu, falls noch nicht vorhanden.
     * @param user Zu prüfender Benutzer.
     */
    public void addIfNotExists(User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) return;
        }
        users.add(user);
    }

    /**
     * Sucht Benutzer anhand des Benutzernamens.
     * @param  username Such-Benutzername.
     * @return          Gefundener Benutzer oder {@code null}.
     */
    public User getByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Lädt einen UserManager aus einer Datei.
     * @param  filename                   Dateipfad.
     * @return                          Geladener oder neuer UserManager.
     * @throws IOException              Lesefehler.
     * @throws ClassNotFoundException   Klasse nicht gefunden.
     */
    public static UserManager loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject();
            if (obj instanceof UserManager) {
                return (UserManager) obj;
            } else {
                throw new IOException("Ungültiges UserManager-Objekt in Datei.");
            }
        } catch (FileNotFoundException e) {
            // Wenn keine Datei da ist, neuen Manager erstellen.
            return new UserManager();
        }
    }

    /**
     * Speichert diesen UserManager in eine Datei.
     * @param  filename      Dateipfad.
     * @throws IOException   Schreibfehler.
     */
    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    /**
     * Gibt alle Benutzer zurück.
     * @return Liste aller Benutzer.
     */
    public List<User> getAllUsers() {
        return users;
    }
}
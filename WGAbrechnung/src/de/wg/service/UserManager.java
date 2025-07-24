package de.wg.service;

import de.wg.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    public void addIfNotExists(User user) {
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) return;
        }
        users.add(user);
    }

    public User getByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static UserManager loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject();
            if (obj instanceof UserManager) {
                return (UserManager) obj;
            } else {
                throw new IOException("Ungültiges UserManager-Objekt in Datei.");
            }
        } catch (FileNotFoundException e) {
            return new UserManager();
        }
    }

    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    public List<User> getAllUsers() {
        return users;
    }
}

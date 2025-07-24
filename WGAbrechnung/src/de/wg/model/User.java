package de.wg.model;

import java.io.Serializable;
import java.util.Objects;

public class User extends Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String passwordHash;
    private boolean isAdmin;

    public User(String name, String username, String passwordHash, boolean isAdmin) {
        super(name);
        this.username = username;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean verifyPassword(String inputHash) {
        return Objects.equals(passwordHash, inputHash);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public String toString() {
        return username + " (" + getName() + ")" + (isAdmin ? " [Admin]" : "");
    }
}

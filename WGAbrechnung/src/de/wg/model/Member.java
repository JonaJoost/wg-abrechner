package de.wg.model;

import java.io.Serializable;
/**
 * Repräsentiert ein WG-Mitglied.
 * Erbt von Person und implementiert AccountHolder.
 */
public class Member extends Person implements AccountHolder, Serializable {
    private static final long serialVersionUID = 1L;
    
    private Account account; // Account-Attribut für das Konto des Mitglieds
    
    public Member(String name) { // Konstruktor
        super(name); // ruft Konstruktor von Person auf
        this.account = new Account(this); // erstellt neues Account-Objekt
    }
    
    public Account getAccount() {
        return account;
    }
    
    public void setAccount(Account account) { 
        this.account = account;
    }
    
    public String getName() { // Getter für Name
        return super.getName(); // nutzt getName() der Elternklasse
    }
}
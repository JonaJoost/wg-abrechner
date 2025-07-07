// Konto-Klasse für WG-Mitglieder
package de.wg.model;

import java.io.Serializable;	// Interface was ermöglicht Objekte in Bytes umzuwandeln (zum Speichern)

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Member owner;	// Besitzer des Kontos
	private double balance = 0.0;	//Schulden oder Guthaben
	
	public Account(Member owner) {	//Konstruktor
		this.owner = owner;
	}
	
	public double getBalance() {	//Methode für Kontostand
		return balance;
	}
	
	public void updateBalance(double delta) {	//verändert kontostand
		balance += delta;
	}
	
	public Member getOwner() {	//gibt das zugehörige mitglied zurück
		return owner;
	}
	
}

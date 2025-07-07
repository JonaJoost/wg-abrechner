// Konto-Klasse f�r WG-Mitglieder
package de.wg.model;

import java.io.Serializable;	// Interface was erm�glicht Objekte in Bytes umzuwandeln (zum Speichern)

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Member owner;	// Besitzer des Kontos
	private double balance = 0.0;	//Schulden oder Guthaben
	
	public Account(Member owner) {	//Konstruktor
		this.owner = owner;
	}
	
	public double getBalance() {	//Methode f�r Kontostand
		return balance;
	}
	
	public void updateBalance(double delta) {	//ver�ndert kontostand
		balance += delta;
	}
	
	public Member getOwner() {	//gibt das zugeh�rige mitglied zur�ck
		return owner;
	}
	
}

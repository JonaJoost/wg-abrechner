package de.wg.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Transaction {	// Repräsentiert eine einzelne Ausgabe oder Schuldenverteilung
	private static final long serialVersionUID = 1L;
	
	private LocalDate date;			//Datum
	private double amount;			//Betrag
	private Member payer;			//Zahler
	private List<Member> beneficiaries; //Begünstigte
	private String description;		//Beschreibung
	private boolean verrechnet = false;	//verrechnet ja/nein
	
	//Konstruktor
	public Transaction(LocalDate date, double amount, Member payer, List<Member> beneficiaries, String description) {
		if (date == null || payer == null || beneficiaries == null || beneficiaries.isEmpty()) {
			throw new IllegalArgumentException("Ungültige Transaktionsdaten");
		}		//Fehlerbehandlung
	this.date = date;
	this.amount = amount;
	this.payer = payer;
	this.beneficiaries = beneficiaries;
	this.description = description;
	}
	//Getter
	public LocalDate getDate() {
		return date;
	}

	public double getAmount() {
		return amount;
	}

	public Member getPayer() {
		return payer;
	}

	public List<Member> getBeneficiaries() {
		return beneficiaries;
	}

	public String getDescription() {
		return description;
	}

	public boolean isVerrechnet() {
		return verrechnet;
	}

	public void setVerrechnet(boolean verrechnet) {
		this.verrechnet = verrechnet;
	}
	@Override
	public String toString() {
		return date + ": " + payer.getName() + " zahlte " + amount + " EUR für " + beneficiaries.size() + " Personen (" + description + ")";		
	}
}

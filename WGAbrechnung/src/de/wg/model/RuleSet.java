package de.wg.model;

/**
 * Enthält Regeln für die Anwendung z.B. maximale Schulden. Diese Regeln
 * führen nicht zu Fehlern, sondern liefern Hinweistexte.
 */
public class RuleSet {

	/** Die maximal erlaubte Schuldenhöhe in Euro. */
	private double maxSchulden = 100.0;

	/** Die maximal erlaubte Verleihdauer in Tagen. */
	private int maxVerleihdauerTage = 30;

	/**
	 * Erstellt ein neues {@code RuleSet} mit Standardwerten.
	 */
	public RuleSet() {
	}

	/**
	 * Gibt die maximal erlaubte Schuldenhöhe zurück.
	 *
	 * @return der aktuelle Grenzwert für Schulden in Euro
	 */
	public double getMaxSchulden() {
		return maxSchulden;
	}

	/**
	 * Setzt die maximal erlaubte Schuldenhöhe.
	 *
	 * @param maxSchulden der neue Grenzwert für Schulden in Euro
	 */
	public void setMaxSchulden(double maxSchulden) {
		this.maxSchulden = maxSchulden;
	}

	/**
	 * Gibt einen Hinweistext zurück, wenn die Schulden einer Person das Limit
	 * überschreiten.
	 *
	 * @param name  der Name der betroffenen Person
	 * @param saldo der aktuelle Kontostand (negativ bei Schulden)
	 * @return ein Hinweistext, wenn das Schuldenlimit überschritten ist, sonst
	 *         {@code null}
	 */
	public String getHinweisWennSchuldenZuHoch(String name, double saldo) {
		if (saldo < -maxSchulden) {
			return "Hinweis: " + name + " hat " + saldo + " EUR Schulden und �berschreitet das Limit von " + maxSchulden
					+ " EUR.";
		}
		return null;
	}
}

package de.wg.model;

/**
 * Enth�lt Regeln f�r die Anwendung � z.B. maximale Schulden.
 * Diese Regeln f�hren nicht zu Fehlern, sondern liefern Hinweistexte.
 */
public class RuleSet {

    // Die maximale erlaubte Schuldenh�he
    private double maxSchulden = 100.0;

    // Optional: maximale Verleihdauer in Tagen (noch nicht verwendet)
    private int maxVerleihdauerTage = 30;

    // Konstruktor mit Standardwerten
    public RuleSet() {
    }

    // Getter und Setter f�r maxSchulden
    public double getMaxSchulden() {
        return maxSchulden;
    }

    public void setMaxSchulden(double maxSchulden) {
        this.maxSchulden = maxSchulden;
    }

    // Hinweistext zur�ckgeben, wenn jemand zu hohe Schulden hat
    public String getHinweisWennSchuldenZuHoch(String name, double saldo) {
        if (saldo < -maxSchulden) {
            return "Hinweis: " + name + " hat " + saldo + " EUR Schulden und �berschreitet das Limit von " + maxSchulden + " EUR.";
        }
        return null; // kein Hinweis n�tig
    }
}

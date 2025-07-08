// Konto-Klasse für WG-Mitglieder
package de.wg.model;

import java.io.Serializable;    // Interface was ermöglicht Objekte in Bytes umzuwandeln (zum Speichern)

/**
 * Repräsentiert ein Konto eines WG-Mitglieds.
 * Jedes Mitglied ({@link Member}) besitzt ein solches Konto,
 * auf dem dessen aktueller Saldo (Guthaben oder Schulden) geführt wird.
 * <p>
 * Die Klasse ist {@link Serializable} für die Datenpersistenz
 * und {@link Comparable} für die Sortierung nach Kontostand (Schulden zuerst).
 * </p>
 * @author Jona
 * @version 1.0
 * @since 2024-07-08
 */
public class Account implements Serializable, Comparable<Account> {
    private static final long serialVersionUID = 1L;

    private Member owner;    // Besitzer des Kontos
    private double balance = 0.0;    // Schulden oder Guthaben

    /**
     * Konstruktor für ein neues Konto.
     * Ein Konto wird immer einem spezifischen WG-Mitglied zugeordnet.
     *
     * @param owner Das {@link Member}-Objekt, dem dieses Konto gehört.
     */
    public Account(Member owner) {
        this.owner = owner;
    }

    /**
     * Gibt den aktuellen Kontostand (Saldo) zurück.
     * Ein negativer Wert bedeutet Schulden, ein positiver Wert Guthaben.
     *
     * @return Der aktuelle Kontostand des Mitglieds.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Aktualisiert den Kontostand um den angegebenen Delta-Wert.
     * Ein positiver Delta-Wert erhöht das Guthaben, ein negativer verringert es (erhöht Schulden).
     *
     * @param delta Der Betrag, um den der Kontostand geändert werden soll.
     */
    public void updateBalance(double delta) {
        balance += delta;
    }

    /**
     * Gibt das WG-Mitglied zurück, dem dieses Konto gehört.
     *
     * @return Das {@link Member}-Objekt, der Besitzer des Kontos.
     */
    public Member getOwner() {
        return owner;
    }

    /**
     * Vergleicht dieses Account-Objekt mit dem angegebenen Account-Objekt basierend auf dem Kontostand.
     * Konten mit höheren Schulden (kleinerer Saldo) erscheinen vor Konten mit weniger Schulden oder Guthaben.
     *
     * @param other Das andere Account-Objekt, mit dem verglichen werden soll.
     * @return Ein negativer Integer, null oder ein positiver Integer, wenn dieses Konto
     * einen geringeren, gleichen oder höheren Saldo als das angegebene Konto hat (Schulden zuerst).
     */
    @Override
    public int compareTo(Account other) {
        // Double.compare(a, b) gibt <0 zurück, wenn a < b, 0 wenn a == b, >0 wenn a > b.
        // Um Schulden zuerst (kleinere Werte zuerst) zu sortieren, verwenden wir direkt this.balance und other.balance.
        return Double.compare(this.balance, other.getBalance());
    }
}
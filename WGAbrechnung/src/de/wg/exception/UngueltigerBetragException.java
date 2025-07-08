package de.wg.exception;

/**
 * Diese Exception wird geworfen, wenn eine Transaktion
 * einen ungültigen Betrag (z.B. 0 oder negativ) enthält.
 */
public class UngueltigerBetragException extends Exception {

    // Konstruktor, der die Fehlermeldung weitergibt
    public UngueltigerBetragException(String message) {
        super(message);
    }
}

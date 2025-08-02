package de.wg.exception;

/**
 * Exception für ungültigen Betrag als Transaktionen
 */
public class UngueltigerBetragException extends Exception {

	public UngueltigerBetragException(String message) {
		super(message);
	}
}

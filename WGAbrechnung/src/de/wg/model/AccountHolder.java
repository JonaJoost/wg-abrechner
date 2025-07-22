package de.wg.model;

import de.wg.ui.*;

/**
 * Interface für Objekte, die ein Konto besitzen können. Wird von Klassen
 * implementiert, die Account-Funktionalität benötigen.
 */
public interface AccountHolder {

	/**
	 * Gibt das Konto des Account-Inhabers zurück.
	 * 
	 * @return Das Account-Objekt
	 */
	Account getAccount();

	/**
	 * Gibt den Namen des Account-Inhabers zurück.
	 * 
	 * @return Der Name als String
	 */
	String getName();
}
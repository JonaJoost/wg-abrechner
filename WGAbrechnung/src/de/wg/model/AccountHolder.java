package de.wg.model;

/**
 * Interface f�r Objekte, die ein Konto besitzen k�nnen.
 * Wird von Klassen implementiert, die Account-Funktionalit�t ben�tigen.
 */
public interface AccountHolder {
    
    /**
     * Gibt das Konto des Account-Inhabers zur�ck.
     * @return Das Account-Objekt
     */
    Account getAccount();
    
    /**
     * Gibt den Namen des Account-Inhabers zur�ck.
     * @return Der Name als String
     */
    String getName();
}
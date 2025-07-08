package de.wg.service;

import de.wg.model.User;
import de.wg.model.RuleSet;
import de.wg.model.AccountHolder;

/**
 * Klasse zur Verwaltung der Benutzeranmeldung und Prüfung von Schuldenregeln
 */
public class LoginManager {

    private RuleSet ruleSet;

    // Konstruktor mit Regelwerk
    public LoginManager(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    // Führt einen Login durch und gibt Textmeldung zurück
    public String login(User user, String eingegebenerHash) {
        if (!user.verifyPassword(eingegebenerHash)) {
            return "Login fehlgeschlagen: Falsches Passwort.";
        }

        String hinweis = null;

        // Prüfung ob User Account-Funktionalität hat
        if (user instanceof AccountHolder) {
            AccountHolder accountHolder = (AccountHolder) user;
            double saldo = accountHolder.getAccount().getBalance();
            hinweis = ruleSet.getHinweisWennSchuldenZuHoch(accountHolder.getName(), saldo);
        }

        if (hinweis != null) {
            return "Login erfolgreich.\n" + hinweis;
        } else {
            return "Login erfolgreich.";
        }
    }
}
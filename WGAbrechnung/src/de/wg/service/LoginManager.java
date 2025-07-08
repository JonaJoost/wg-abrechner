package de.wg.service;

import de.wg.model.User;
import de.wg.model.RuleSet;
import de.wg.model.AccountHolder;

/**
 * Klasse zur Verwaltung der Benutzeranmeldung und Pr�fung von Schuldenregeln
 */
public class LoginManager {

    private RuleSet ruleSet;

    // Konstruktor mit Regelwerk
    public LoginManager(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    // F�hrt einen Login durch und gibt Textmeldung zur�ck
    public String login(User user, String eingegebenerHash) {
        if (!user.verifyPassword(eingegebenerHash)) {
            return "Login fehlgeschlagen: Falsches Passwort.";
        }

        String hinweis = null;

        // Pr�fung ob User Account-Funktionalit�t hat
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
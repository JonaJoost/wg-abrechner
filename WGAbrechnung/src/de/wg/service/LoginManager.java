// Klasse zur Verwaltung der Benutzeranmeldung und Pr�fung von Schuldenregeln
package de.wg.service;

import de.wg.model.Member;
import de.wg.model.User;
import de.wg.model.RuleSet;

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

        // Wenn User gleichzeitig ein Member ist -> Saldo pr�fen
        if (user instanceof Member) {
            Member m = (Member) user;
            double saldo = m.getAccount().getBalance();
            hinweis = ruleSet.getHinweisWennSchuldenZuHoch(m.getName(), saldo);
        }

        if (hinweis != null) {
            return "Login erfolgreich.\n" + hinweis;
        } else {
            return "Login erfolgreich.";
        }
    }
} 

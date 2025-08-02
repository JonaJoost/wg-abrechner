package de.wg.service;

import de.wg.model.User;
import de.wg.model.RuleSet;
import de.wg.model.AccountHolder;

/**
 * Die Klasse {@code LoginManager} verwaltet die Benutzeranmeldung und prüft
 * beim erfolgreichen Login, ob Schuldenregeln verletzt wurden.
 * 
 * Wenn der Benutzer ein {@link AccountHolder} ist, wird anhand eines
 * {@link RuleSet} geprüft, ob der Kontostand das definierte Schuldenlimit
 * überschreitet.
 */
public class LoginManager {

	private RuleSet ruleSet;

	/**
	 * Erstellt einen {@code LoginManager} mit einem übergebenen Regelwerk.
	 *
	 * @param ruleSet das zu verwendende Regelwerk zur Schuldenprüfung
	 */
	public LoginManager(RuleSet ruleSet) {
		this.ruleSet = ruleSet;
	}

	/**
	 * Führt den Login-Vorgang für einen Benutzer durch und gibt eine Textmeldung
	 * zurück. Es wird geprüft, ob das eingegebene Passwort korrekt ist. Zusätzlich
	 * wird bei {@link AccountHolder}-Benutzern geprüft, ob Schulden das erlaubte
	 * Limit überschreiten.
	 *
	 * @param user             der Benutzer, der sich anmelden möchte
	 * @param eingegebenerHash der Hash des eingegebenen Passworts
	 * @return eine Textmeldung über Erfolg oder Misserfolg des Logins, ggf. mit
	 *         Schuldenhinweis
	 */
	public String login(User user, String eingegebenerHash) {
		if (!user.verifyPassword(eingegebenerHash)) {
			return "Login fehlgeschlagen: Falsches Passwort.";
		}

		String hinweis = null;

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
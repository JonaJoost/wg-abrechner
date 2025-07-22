package de.wg.service;

import de.wg.model.Member;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Verwaltungsklasse für Member-Objekte. Enthält Methoden zum Hinzufügen,
 * Suchen, Sortieren, Speichern und Laden von Mitgliedern.
 */
public class MemberManager implements Serializable {

	private static final long serialVersionUID = 1L; 
	private List<Member> members;

	/**
	 * Konstruktor für den MemberManager. Initialisiert die interne Liste für die
	 * Mitglieder.
	 */
	public MemberManager() {
		this.members = new ArrayList<>();
	}

	/**
	 * Fügt ein neues Mitglied zur Verwaltung hinzu.
	 *
	 * @param member Das hinzuzufügende Member-Objekt. Darf nicht null sein.
	 * @throws IllegalArgumentException Wenn das übergebene Member-Objekt null ist.
	 */
	public void addMember(Member member) {
		if (member == null) {
			throw new IllegalArgumentException("Member darf nicht null sein.");
		}
		this.members.add(member);
	}

	/**
	 * Gibt eine unveränderliche Liste aller verwalteten Mitglieder zurück.
	 *
	 * @return Eine unveränderliche Liste von Member-Objekten.
	 */
	public List<Member> getAllMembers() {
		return Collections.unmodifiableList(members);
	}

	/**
	 * Sucht ein Mitglied anhand seines Namens.
	 *
	 * @param name Der Name des zu suchenden Mitglieds.
	 * @return Das Member-Objekt, falls gefunden, sonst null.
	 */
	public Member getMemberByName(String name) {
		for (Member member : members) {
			if (member.getName().equals(name)) {
				return member;
			}
		}
		return null;
	}

	/**
	 * Entfernt ein Mitglied aus der Verwaltung.
	 *
	 * @param member Das zu entfernende Member-Objekt.
	 * @return True, wenn das Mitglied erfolgreich entfernt wurde, sonst false.
	 */
	public boolean removeMember(Member member) {
		return this.members.remove(member);
	}

	/**
	 * Sortiert die Mitglieder alphabetisch nach Namen.
	 */
	public void sortMembersByName() {
		Collections.sort(this.members, Comparator.comparing(Member::getName));
	}

	/**
	 * Speichert den aktuellen Zustand des MemberManager-Objekts in einer Datei.
	 *
	 * @param filename Der Pfad und Dateiname, unter dem die Daten gespeichert
	 *                 werden sollen.
	 * @throws IOException Falls ein Fehler beim Schreiben der Datei auftritt.
	 */
	public void saveToFile(String filename) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
			oos.writeObject(this);
			System.out.println("MemberManager erfolgreich in " + filename + " gespeichert.");
		}
	}

	/**
	 * Lädt ein MemberManager-Objekt aus einer Datei. Wenn die Datei nicht gefunden
	 * wird, wird ein neuer, leerer MemberManager zurückgegeben.
	 *
	 * @param filename Der Pfad und Dateiname, aus dem die Daten geladen werden
	 *                 sollen.
	 * @return Das geladene MemberManager-Objekt.
	 * @throws IOException            Falls ein anderer Fehler beim Lesen der Datei
	 *                                auftritt (außer FileNotFoundException).
	 * @throws ClassNotFoundException Falls die Klasse des serialisierten Objekts
	 *                                nicht gefunden wird.
	 */
	public static MemberManager loadFromFile(String filename) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			Object obj = ois.readObject(); // Liest das Objekt aus der Datei
			if (obj instanceof MemberManager) {
				System.out.println("MemberManager erfolgreich aus " + filename + " geladen.");
				return (MemberManager) obj; // Castet es zurück zum MemberManager
			} else {
				throw new IOException("Datei enthält kein gültiges MemberManager-Objekt.");
			}
		} catch (FileNotFoundException e) {
			System.out.println(
					"Die Datei '" + filename + "' wurde nicht gefunden. Ein neuer MemberManager wird erstellt.");
			return new MemberManager(); // Wenn Datei nicht existiert, starte mit neuem Manager
		}
	}
}
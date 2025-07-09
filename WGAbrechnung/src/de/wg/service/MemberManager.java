package de.wg.service;

import de.wg.model.Member;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Verwaltungsklasse f�r Member-Objekte.
 * Enth�lt Methoden zum Hinzuf�gen, Suchen, Sortieren, Speichern und Laden von Mitgliedern.
 */
public class MemberManager implements Serializable {

    private static final long serialVersionUID = 1L; // Versions-ID f�r die Serialisierung
    private List<Member> members; // Liste zur Speicherung der Member-Objekte

    /**
     * Konstruktor f�r den MemberManager.
     * Initialisiert die interne Liste f�r die Mitglieder.
     */
    public MemberManager() {
        this.members = new ArrayList<>();
    }

    /**
     * F�gt ein neues Mitglied zur Verwaltung hinzu.
     *
     * @param member Das hinzuzuf�gende Member-Objekt. Darf nicht null sein.
     * @throws IllegalArgumentException Wenn das �bergebene Member-Objekt null ist.
     */
    public void addMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member darf nicht null sein.");
        }
        this.members.add(member);
    }

    /**
     * Gibt eine unver�nderliche Liste aller verwalteten Mitglieder zur�ck.
     *
     * @return Eine unver�nderliche Liste von Member-Objekten.
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
     * @param filename Der Pfad und Dateiname, unter dem die Daten gespeichert werden sollen.
     * @throws IOException Falls ein Fehler beim Schreiben der Datei auftritt.
     */
    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this); // Schreibt das gesamte MemberManager-Objekt
            System.out.println("MemberManager erfolgreich in " + filename + " gespeichert.");
        }
    }

    /**
     * L�dt ein MemberManager-Objekt aus einer Datei.
     * Wenn die Datei nicht gefunden wird, wird ein neuer, leerer MemberManager zur�ckgegeben.
     *
     * @param filename Der Pfad und Dateiname, aus dem die Daten geladen werden sollen.
     * @return Das geladene MemberManager-Objekt.
     * @throws IOException            Falls ein anderer Fehler beim Lesen der Datei auftritt (au�er FileNotFoundException).
     * @throws ClassNotFoundException Falls die Klasse des serialisierten Objekts nicht gefunden wird.
     */
    public static MemberManager loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject(); // Liest das Objekt aus der Datei
            if (obj instanceof MemberManager) {
                System.out.println("MemberManager erfolgreich aus " + filename + " geladen.");
                return (MemberManager) obj; // Castet es zur�ck zum MemberManager
            } else {
                throw new IOException("Datei enth�lt kein g�ltiges MemberManager-Objekt.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Die Datei '" + filename + "' wurde nicht gefunden. Ein neuer MemberManager wird erstellt.");
            return new MemberManager(); // Wenn Datei nicht existiert, starte mit neuem Manager
        }
    }
}
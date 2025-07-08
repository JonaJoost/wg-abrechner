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
     * @return Eine Liste aller Mitglieder.
     */
    public List<Member> getAllMembers() {
        return Collections.unmodifiableList(members);
    }

    /**
     * Sucht Mitglieder anhand eines Teils ihres Namens.
     * Die Suche ist nicht Gro�- / Kleinschreibung-sensitiv.
     *
     * @param searchTerm Der Suchbegriff (Teil des Namens).
     * @return Eine Liste von Mitgliedern, deren Namen den Suchbegriff enthalten.
     * Gibt eine leere Liste zur�ck, wenn der Suchbegriff null oder leer ist.
     */
    public List<Member> searchMembersByName(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return new ArrayList<>();
        }
        String lowerCaseSearchTerm = searchTerm.toLowerCase();
        return members.stream()
                .filter(member -> member.getName().toLowerCase().contains(lowerCaseSearchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Sortiert alle Mitglieder alphabetisch nach ihrem Namen.
     *
     * @return Eine neue Liste der Mitglieder, sortiert nach Namen.
     */
    public List<Member> sortMembersByName() {
        List<Member> sortedMembers = new ArrayList<>(members);
        Collections.sort(sortedMembers, Comparator.comparing(Member::getName));
        return sortedMembers;
    }

    /**
     * Speichert den aktuellen Zustand des MemberManager-Objekts (und damit alle verwalteten Mitglieder)
     * in einer Datei unter dem angegebenen Dateinamen.
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
package de.wg.service;

import de.wg.model.Member;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Verwaltungsklasse für Member-Objekte.
 * Enthält Methoden zum Hinzufügen, Suchen und Sortieren von Mitgliedern.
 */
public class MemberManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Member> members;

    public MemberManager() {
        this.members = new ArrayList<>();
    }

    /**
     * Fügt ein neues Mitglied zur Verwaltung hinzu.
     *
     * @param member Das hinzuzufügende Member-Objekt.
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
     * @return Eine Liste aller Mitglieder.
     */
    public List<Member> getAllMembers() {
        return Collections.unmodifiableList(members);
    }

    /**
     * Sucht Mitglieder anhand eines Teils ihres Namens.
     * Die Suche ist nicht Groß- / Kleinschreibung-sensitiv.
     *
     * @param searchTerm Der Suchbegriff (Teil des Namens).
     * @return Eine Liste von Mitgliedern, deren Namen den Suchbegriff enthalten.
     */
    public List<Member> searchMembersByName(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return new ArrayList<>(); // Oder alle Mitglieder zurückgeben, je nach Anforderung
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
}
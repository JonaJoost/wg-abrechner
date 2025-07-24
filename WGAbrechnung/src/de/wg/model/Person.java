package de.wg.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Repräsentiert eine allgemeine Person mit einem Namen. Diese Klasse
 * implementiert {@link Serializable} für die Objekt-Serialisierung und
 * {@link Comparable}, um Personen anhand ihres Namens vergleichen zu können.
 */
public class Person implements Serializable, Comparable<Person> {
	private static final long serialVersionUID = 1L;

	/** Der Name der Person. */
	protected String name;

	/**
	 * Konstruktor zur Initialisierung einer Person mit einem Namen.
	 *
	 * @param name der Name der Person; darf nicht {@code null} oder leer sein
	 * @throws IllegalArgumentException wenn der Name {@code null} oder leer ist
	 */
	public Person(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name darf nicht leer sein");
		}
		this.name = name;
	}

	/**
	 * Gibt den Namen der Person zurück.
	 *
	 * @return der Name der Person
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzt den Namen der Person.
	 *
	 * @param name der neue Name; darf nicht {@code null} oder leer sein
	 * @throws IllegalArgumentException wenn der Name {@code null} oder leer ist
	 */
	public void setName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name darf nicht leer sein");
		}
		this.name = name;
	}

	/**
	 * Gibt den Namen der Person als String zurück.
	 * * @return der Name der Person
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Vergleicht zwei Personen anhand ihres Namens alphabetisch.
	 *
	 * @param other die andere Person zum Vergleich
	 * @return ein negativer Integer, wenn diese Person lexikografisch vor
	 * {@code other} liegt, null wenn sie gleich sind, ein positiver Integer
	 * sonst
	 */
	@Override
	public int compareTo(Person other) {
		return this.name.compareTo(other.getName());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return Objects.equals(name, person.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
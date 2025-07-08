//Basisklasse für Personen (wird von member oder user erweitert)
package de.wg.model;

import java.io.Serializable;

public class Person implements Serializable, Comparable<Person> {
	private static final long serialVersionUID = 1L;
	
	protected String name;
	
	public Person(String name) {
		if (name == null || name.isBlank() ) {
			throw new IllegalArgumentException("Name darf nicht leer sein");
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null || name.isBlank() ) {
			throw new IllegalArgumentException("Name darf nicht leer sein");
		}
		this.name = name;
	}
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(Person other) {
	    return this.name.compareTo(other.getName());
	}

}

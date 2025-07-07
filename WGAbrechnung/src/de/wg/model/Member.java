package de.wg.model;

public class Member {
	private String name;  // Attribut
	
	public Member(String name) {	// Konstruktor
		this.name = name;
	}
	
	public String getName() {	// Getter (kein Setter daher ist 
		return name;			// 		   Objekt nach Erstellung unveränderlich)
	}

}

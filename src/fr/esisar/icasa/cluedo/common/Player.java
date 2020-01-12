package fr.esisar.icasa.cluedo.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player {

	private UUID uuid;
	private Person2 person;
	private String name;
	private List<Card> cards;
	
	public Player(Person2 person, String name) {
		super();
		this.uuid = UUID.randomUUID();
		this.person = person;
		this.name = name;
		this.cards = new ArrayList<Card>();
	}

	public UUID getUuid() {
		return uuid;
	}

	public Person2 getPerson() {
		return person;
	}
	
	public String getName() {
		return name;
	}

	public List<Card> getCards() {
		return cards;
	}
	
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	@Override
	public String toString() {
		return "Player [person=" + person + ", cards=" + cards + "]";
	}
}

package fr.esisar.icasa.cluedo.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player {

	private UUID uuid;
	private Person person;
	private String name;
	private List<Card> cards;
	
	public Player(Person person, String name) {
		super();
		this.uuid = UUID.randomUUID();
		this.person = person;
		this.name = name;
		this.cards = new ArrayList<Card>();
	}

	public UUID getUuid() {
		return uuid;
	}

	public Person getPerson() {
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
		return "Player [name=" + name + ", person=" + person + ", cards=" + cards + "]";
	}

	public Clue getClue(Crime supposition) {
		if(cards.contains(supposition.getPerson()))
			return new Clue(supposition.getPerson(), this);
		if(cards.contains(supposition.getRoom()))
			return new Clue(supposition.getRoom(), this);
		if(cards.contains(supposition.getWeapon()))
			return new Clue(supposition.getWeapon(), this);
		else
			return null;
	}
}

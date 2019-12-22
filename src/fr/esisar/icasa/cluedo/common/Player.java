package fr.esisar.icasa.cluedo.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player {

	private UUID uuid;
	private Person person;
	private List<String> cards;
	
	public Player(UUID uuid, Person person) {
		super();
		this.uuid = uuid;
		this.person = person;
		this.cards = new ArrayList<String>();
	}

	public UUID getUuid() {
		return uuid;
	}

	public Person getPerson() {
		return person;
	}

	public List<String> getCards() {
		return cards;
	}
	
	public void setCards(List<String> cards) {
		this.cards = cards;
	}

	@Override
	public String toString() {
		return "Player [person=" + person + ", cards=" + cards + "]";
	}
}

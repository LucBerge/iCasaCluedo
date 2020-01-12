package fr.esisar.icasa.cluedo.common;

import fr.esisar.icasa.cluedo.common.Card;

public class Crime {
	private Card person;
	private Card weapon;
	private Card room;
	
	public Crime(Card person, Card weapon, Card room) {
		super();
		this.person = person;
		this.weapon = weapon;
		this.room = room;
	}

	public Card getPerson() {
		return person;
	}

	public Card getWeapon() {
		return weapon;
	}

	public Card getRoom() {
		return room;
	}

	@Override
	public String toString() {
		return "Le crime a été commis par " + person.getName() + " avec le/la " + weapon.getName() + " dans le/la " + room.getName() + ".";
	}
}

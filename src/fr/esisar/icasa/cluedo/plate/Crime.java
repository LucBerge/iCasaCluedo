package fr.esisar.icasa.cluedo.plate;

public class Crime {
	private String person;
	private String weapon;
	private String room;
	
	public Crime(String person, String weapon, String room) {
		super();
		this.person = person;
		this.weapon = weapon;
		this.room = room;
	}

	public String getPerson() {
		return person;
	}

	public String getWeapon() {
		return weapon;
	}

	public String getRoom() {
		return room;
	}

	@Override
	public String toString() {
		return "Le crime a été commis par " + person + " avec le/la " + weapon + " dans le/la " + room + ".";
	}
}

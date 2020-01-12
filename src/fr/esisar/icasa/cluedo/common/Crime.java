package fr.esisar.icasa.cluedo.common;

public class Crime {
	private Person person;
	private Weapon weapon;
	private Room room;

	public static Crime getRandom() {
		return new Crime(Person.getRandom(), Weapon.getRandom(), Room.getRandom());
	}
	
	public Crime(Person person, Weapon weapon, Room room) {
		super();
		this.person = person;
		this.weapon = weapon;
		this.room = room;
	}

	public Person getPerson() {
		return person;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public Room getRoom() {
		return room;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((person == null) ? 0 : person.hashCode());
		result = prime * result + ((room == null) ? 0 : room.hashCode());
		result = prime * result + ((weapon == null) ? 0 : weapon.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Crime other = (Crime) obj;
		if (person == null) {
			if (other.person != null)
				return false;
		} else if (!person.equals(other.person))
			return false;
		if (room == null) {
			if (other.room != null)
				return false;
		} else if (!room.equals(other.room))
			return false;
		if (weapon == null) {
			if (other.weapon != null)
				return false;
		} else if (!weapon.equals(other.weapon))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Le crime a été commis par " + person.getName() + " avec le/la " + weapon.getName() + " dans le/la " + room.getName() + ".";
	}
}

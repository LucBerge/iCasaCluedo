package fr.esisar.icasa.cluedo.common;

public class Supposition {

	private Player player;
	private Crime crime;
	
	public Supposition(Player player, Crime crime) {
		super();
		this.player = player;
		this.crime = crime;
	}

	public Player getPlayer() {
		return player;
	}

	public Crime getCrime() {
		return crime;
	}

	@Override
	public String toString() {
		return player.getName() + " fait la supposition que " + crime.getPerson() + " a tué avec le/la " + crime.getWeapon() + " dans le/la " + crime.getRoom();
	}	
}

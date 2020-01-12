package fr.esisar.icasa.cluedo.common;

public class Clue {
	
	private Card card;
	private Player player;
	
	public Clue(Card card, Player player) {
		super();
		this.card = card;
		this.player = player;
	}
	
	public Card getCard() {
		return card;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public String toString() {
		return getPlayer().getName() + " montre la carte " + getCard().getName();
	}
}

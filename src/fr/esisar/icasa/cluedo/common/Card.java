package fr.esisar.icasa.cluedo.common;

public class Card {

    /**
     * The corresponding name
     */
    private String name;
    
    protected Card(String name) {
    	this.name = name;
    }
    
    /**
     * get the name of the card
     * 
     * @return The name of the card
     */
    public String getName() {
        return name;
    }

	@Override
	public String toString() {
		return name;
	}
}

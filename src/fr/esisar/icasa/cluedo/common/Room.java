package fr.esisar.icasa.cluedo.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Room extends Card{
    public static final Room CONSERVATOIRE = new Room("Conservatoire");
    public static final Room CUISINE = new Room("Cuisine");
    public static final Room SALLE_A_MANGER = new Room("Salle à Manger");
    public static final Room SALLE_DE_BALLE = new Room("Salle de Bal");
    public static final Room BUREAU = new Room("Bureau");
    public static final Room SALLE_DE_BILLARD = new Room("Salle de Billard");
    public static final Room VERANDA = new Room("Véranda");
    public static final Room BIBLIOTHEQUE = new Room("Bibliothèque");
    public static final Room HALL = new Room("Hall");

    public static final Room[] ALL = {CONSERVATOIRE, CUISINE, SALLE_A_MANGER, SALLE_DE_BALLE, BUREAU, SALLE_DE_BILLARD, VERANDA, BIBLIOTHEQUE, HALL};
    
    private Room(String name) {
    	super(name);
    }

    public static Room getRandom() {
    	List<Room> suffled = new ArrayList<Room>();
    	suffled.addAll(Arrays.asList(ALL));
		Collections.shuffle(suffled);
		return suffled.get(0);
    }
    
    public static Room fromName(String name) {
    	for(Room room:ALL) {
    		if(room.getName().contentEquals(name))
    			return room;
    	}
    	return null;
    }
}

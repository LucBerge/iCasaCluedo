package fr.esisar.icasa.cluedo.common;

public class Room extends Card{
    public static final Room CONSERVATOIRE = new Room("Conservatoire");
    public static final Room CUISINE = new Room("Cuisine");
    public static final Room SALLE_A_MANGER = new Room("Salle à manger");
    public static final Room SALLE_DE_BALLE = new Room("Salle de bal");
    public static final Room BUREAU = new Room("Bureau");
    public static final Room SALLE_DE_BILLARD = new Room("Salle de billard");
    public static final Room VERANDA = new Room("Veranda");
    public static final Room BIBLIOTHEQUE = new Room("Bibliothèque");
    public static final Room HALL = new Room("Hall");

    public static final Room[] ALL = {CONSERVATOIRE, CUISINE, SALLE_A_MANGER, SALLE_DE_BALLE, BUREAU, SALLE_DE_BILLARD, VERANDA, BIBLIOTHEQUE, HALL};
    
    private Room(String name) {
    	super(name);
    }
}

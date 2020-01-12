package fr.esisar.icasa.cluedo.common;

public class Weapon extends Card{
    public static final Weapon TABLETTE = new Weapon("Tablette");
    public static final Weapon LAMPE = new Weapon("Lampe");
    public static final Weapon RADIATEUR = new Weapon("Radiateur");
    public static final Weapon THERMOMETRE = new Weapon("Thermomètre");
    public static final Weapon DETECTEUR = new Weapon("Détecteur de présence");
    public static final Weapon HAUTPAREUR = new Weapon("Hautparleur");
    
    public static final Weapon[] ALL = {TABLETTE, LAMPE, RADIATEUR, THERMOMETRE, DETECTEUR, HAUTPAREUR};
    
    private Weapon(String name) {
    	super(name);
    }
}

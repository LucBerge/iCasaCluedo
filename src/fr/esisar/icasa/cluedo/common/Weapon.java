package fr.esisar.icasa.cluedo.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Weapon extends Card{
    public static final Weapon TABLETTE = new Weapon("Tablette");
    public static final Weapon LAMPE = new Weapon("Lampe");
    public static final Weapon RADIATEUR = new Weapon("Radiateur");
    public static final Weapon THERMOMETRE = new Weapon("Thermomètre");
    public static final Weapon DETECTEUR = new Weapon("Capteur de présence");
    public static final Weapon HAUTPAREUR = new Weapon("Haut parleur");
    
    public static final Weapon[] ALL = {TABLETTE, LAMPE, RADIATEUR, THERMOMETRE, DETECTEUR, HAUTPAREUR};
    
    private Weapon(String name) {
    	super(name);
    }

    public static Weapon getRandom() {
    	List<Weapon> suffled = new ArrayList<Weapon>();
    	suffled.addAll(Arrays.asList(ALL));
		Collections.shuffle(suffled);
		return suffled.get(0);
    }
    
    public static Weapon fromSerialNumber(String name) {
    	for(Weapon weapon:ALL) {
    		if(weapon.getName().contentEquals(name))
    			return weapon;
    	}
    	return null;
    }
}

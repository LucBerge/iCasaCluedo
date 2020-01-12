package fr.esisar.icasa.cluedo.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Person extends Card{
    public static final Person MADAME_LEBLANC = new Person("Madame LeBlanc");
    public static final Person MADAME_PERVENCHE = new Person("Madame Pervenche");
    public static final Person COLONEL_MOUTARDE = new Person("Colonel Moutarde");
    public static final Person MADEMOISELLE_ROSE = new Person("Mademoiselle Rose");
    public static final Person DOCTEUR_OLIVE = new Person("Docteur Olive");
    public static final Person PROFESSEUR_VIOLET = new Person("Professeur Violet");
	
    public static final Person[] ALL = {MADAME_LEBLANC, MADAME_PERVENCHE, COLONEL_MOUTARDE, MADEMOISELLE_ROSE, DOCTEUR_OLIVE, PROFESSEUR_VIOLET};

    private Person(String name) {
    	super(name);
    }

    public static Person getRandom() {
    	List<Person> suffled = new ArrayList<Person>();
    	suffled.addAll(Arrays.asList(ALL));
		Collections.shuffle(suffled);
		return suffled.get(0);
    }
    
    public static Person fromName(String name) {
    	for(Person person:ALL) {
    		if(person.getName().contentEquals(name))
    			return person;
    	}
    	return null;
    }
}

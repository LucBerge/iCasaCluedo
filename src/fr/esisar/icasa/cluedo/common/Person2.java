package fr.esisar.icasa.cluedo.common;

public enum Person2 {
    MADAME_LEBLANC("Madame LeBlanc"),
    MADAME_PERVANCHE("Madame Pervenche"),
    COLONEL_MOUTARDE("Colonel_Moutarde"),
    MADEMOISELLE_ROSE("Mademoiselle Rose"),
    DOCTEUR_OLIVE("Docteur Olive"),
    PROFESSEUR_VIOLET("Professeur Violet");
	 
    /**
     * The corresponding name
     */
    private String name;
 
    /**
     * get the name of the player
     * 
     * @return The name of the player
     */
    public String getName() {
        return name;
    }
    
    private Person2(String name) {
    	this.name = name;
    }
}

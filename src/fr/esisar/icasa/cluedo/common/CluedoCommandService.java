package fr.esisar.icasa.cluedo.common;

public interface CluedoCommandService {    
    /**
     * Gets the number of players
     * @return The number of players.
     */
    public int getNumberOfPlayers();
 
    /**
     * Sets the number of players.
     * @param numberOfPlayers - The new number of players.
     * @throws Exception si impossible de modifier le nombre de joueur
     */
    public void setNumberOfPlayers(int numberOfPlayers) throws Exception;
    
    /**
     * Reset the game
     */
    public void reset();
}

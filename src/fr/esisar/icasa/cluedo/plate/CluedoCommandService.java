package fr.esisar.icasa.cluedo.plate;

public interface CluedoCommandService {    
    /**
     * Gets the number of players
     * @return The number of players.
     */
    public int getNumberOfPlayers();
 
    /**
     * Sets the number of players.
     * @param numberOfPlayers - The new number of players.
     * @throws Exception si impossible de modifier le nombre de joueur.
     */
    public void setNumberOfPlayers(int numberOfPlayers) throws Exception;
    
    /**
     * Reset the game
     */
    public void reset();

	/** 
	 * Allows AIs to join the game.
	 * @param fullAI true to allows AIs.
	 */
	public void setFullAI(boolean fullAI);
}

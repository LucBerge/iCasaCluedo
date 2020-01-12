package fr.esisar.icasa.cluedo.player;

import fr.esisar.icasa.cluedo.common.Crime;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Player;

public interface CluedoPlayerService {
    /**
     * Try to join the current game with a given person
     * @throws Exception 
     */
    public void join(Person person, String name) throws Exception;

    /**
     * Get the current player
     */
    public Player getPlayer();

	/**
	 * @return
	 */
	boolean AICanChoose();

	/**
	 * @return
	 * @throws Exception 
	 */
	boolean myTurn() throws Exception;

	/**
	 * @return
	 */
	boolean isGameStarted();

	/**
	 * @param supposition
	 * @throws Exception 
	 */
	public void suppose(Crime supposition) throws Exception;
}

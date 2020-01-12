package fr.esisar.icasa.cluedo.player;

import fr.esisar.icasa.cluedo.common.Person2;
import fr.esisar.icasa.cluedo.common.Player;

public interface CluedoPlayerService {
    /**
     * Try to join the current game with a given person
     * @throws Exception 
     */
    public void join(Person2 person, String name) throws Exception;

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
	 */
	boolean myTurn();

	/**
	 * @return
	 */
	boolean isGameStarted();
}

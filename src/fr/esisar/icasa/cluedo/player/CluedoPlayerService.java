package fr.esisar.icasa.cluedo.player;

import fr.esisar.icasa.cluedo.common.Crime;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Player;

public interface CluedoPlayerService {
    /**
     * Try to join the current game with a given person.
     * @throws Exception if this person has already been registered or the game has already started.
     */
    public void join(Person person, String name) throws Exception;

    /**
     * Get the current player.
     */
    public Player getPlayer();

	/**
	 * Says if AIs can choose there person.
	 * @return true if they can.
	 */
    public boolean AICanChoose();

	/**
	 * Says if a player can play.
	 * @return true if he can.
	 * @throws Exception if the game is not on.
	 */
    public boolean myTurn() throws Exception;

	/**
	 * Says if the game has started.
	 * @return true if the game has started.
	 */
    public boolean isGameStarted();

	/**
	 * Makes a supposition.
	 * @param supposition The supposition.
	 * @return the corresponding clue.
	 * @throws Exception if it is not you turn.
	 */
	public void suppose(Crime supposition) throws Exception;
}

package fr.esisar.icasa.cluedo.plate;

import fr.esisar.icasa.cluedo.common.Card;
import fr.esisar.icasa.cluedo.common.Crime;
import fr.esisar.icasa.cluedo.common.Person2;
import fr.esisar.icasa.cluedo.common.Player;

public interface CluedoPlateService {
    /**
     * Register the player with a corresponding figure
     * @param person
     * @param name
     * @return the player instance
     * @throws Exception if this player has already been registered or the game already started.
     */
    public Player register(Person2 person, String name) throws Exception;

	/** Say if AIs can choose there person.
	 * @return true if they can.
	 */
	boolean AICanChoose();

	/** Say if a player can play.
	 * @return true if he can.
	 */
	boolean myTurn(Player player);

	/**
	 * @return
	 */
	boolean isGameStarted();

	/**
	 * @param player
	 * @param supposition
	 * @return
	 * @throws Exception
	 */
	Card supposition(Player player, Crime supposition) throws Exception;
}

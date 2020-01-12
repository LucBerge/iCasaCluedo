package fr.esisar.icasa.cluedo.plate;

import fr.esisar.icasa.cluedo.common.Clue;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Player;
import fr.esisar.icasa.cluedo.common.Supposition;

public interface CluedoPlateService {
    /**
     * Register the player with a corresponding figure
     * @param person
     * @param name
     * @return the player instance
     * @throws Exception if this player has already been registered or the game already started.
     */
    public Player register(Person person, String name) throws Exception;

	/** Say if AIs can choose there person.
	 * @return true if they can.
	 */
	boolean AICanChoose();

	/** Say if a player can play.
	 * @return true if he can.
	 * @throws Exception 
	 */
	boolean myTurn(Player player) throws Exception;

	/**
	 * @return
	 */
	boolean isGameStarted();

	/**
	 * @param supposition
	 * @return
	 * @throws Exception
	 */
	Clue supposition(Supposition supposition) throws Exception;
}

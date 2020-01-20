package fr.esisar.icasa.cluedo.plate;

import java.util.List;

import fr.esisar.icasa.cluedo.common.Clue;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Player;
import fr.esisar.icasa.cluedo.common.Supposition;

public interface CluedoPlateService {
    /**
     * Register the player with a corresponding person.
     * @param person
     * @param name
     * @return the player instance
     * @throws Exception if this person has already been registered or the game has already started.
     */
    public Player register(Person person, String name) throws Exception;

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
    public boolean myTurn(Player player) throws Exception;

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
    public Clue supposition(Supposition supposition) throws Exception;

	/** 
	 * Get all the known clues.
	 * @return Known clues.
	 */
    public List<Clue> getClues();
}

package fr.esisar.icasa.cluedo.common;

import fr.esisar.icasa.cluedo.common.Person;

public interface CluedoPlateService {
    /**
     * Register the player with a corresponding figure
     * @param player
     * @return unique UUIDs
     * @throws Exception if this player has already been registered or the game already started.
     */
    public Player register(Person player) throws Exception;
}

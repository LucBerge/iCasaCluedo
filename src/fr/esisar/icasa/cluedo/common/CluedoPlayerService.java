package fr.esisar.icasa.cluedo.common;

public interface CluedoPlayerService {
    /**
     * Try to join the current game with a given person
     */
    public void join(Person person);

    /**
     * Get the current player
     */
    public Player getPlayer();
}

package fr.esisar.icasa.cluedo.command;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;

import fr.esisar.icasa.cluedo.common.Person2;
import fr.esisar.icasa.cluedo.common.Player;
import fr.esisar.icasa.cluedo.player.CluedoPlayerService;
import fr.liglab.adele.icasa.command.handler.Command;
import fr.liglab.adele.icasa.command.handler.CommandProvider;

@Component
@Instantiate(name = "CluedoPlayerCommand")
@CommandProvider(namespace = "cluedo")
public class CluedoPlayerCommand {

	/** Field for CluedoCommandService dependency */
	@Requires
	private CluedoPlayerService cluedoPlayerService;

	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("Stopping " + this.getClass().getName());
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Starting " + this.getClass().getName());
	}

	/**
	 * Join the game with a person.
	 */
	@Command
	public void join(String person) {
		try {
			switch (person) {
			case "COLONEL_MOUTARDE":
				cluedoPlayerService.join(Person2.COLONEL_MOUTARDE, "Joueur");
				break;
			case "DOCTEUR_OLIVE":
				cluedoPlayerService.join(Person2.DOCTEUR_OLIVE, "Joueur");
				break;
			case "MADAME_LEBLANC":
				cluedoPlayerService.join(Person2.MADAME_LEBLANC, "Joueur");
				break;
			case "MADAME_PERVANCHE":
				cluedoPlayerService.join(Person2.MADAME_PERVANCHE, "Joueur");
				break;
			case "MADEMOISELLE_ROSE":
				cluedoPlayerService.join(Person2.MADEMOISELLE_ROSE, "Joueur");
				break;
			case "PROFESSEUR_VIOLET":
				cluedoPlayerService.join(Person2.PROFESSEUR_VIOLET, "Joueur");
				break;
			default:
				System.out.println("Le personnage " + person + " n'existe pas.");
			}
			getPlayer();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Command
	public void getPlayer() {
		Player player = cluedoPlayerService.getPlayer();
		if(player != null)
			System.out.println("Vous jouez avec " + player.getPerson());
		else
			System.out.println("Vous n'avez pas rejoind la partie");
	}
}

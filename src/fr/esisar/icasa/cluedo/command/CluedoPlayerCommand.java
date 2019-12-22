package fr.esisar.icasa.cluedo.command;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;

import fr.esisar.icasa.cluedo.common.CluedoPlayerService;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Player;
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
	 * Felix shell command implementation to sets the illuminance preference.
	 *
	 * @param goal the new illuminance preference ("SOFT", "MEDIUM", "FULL")
	 */
	@Command
	public void join(String person) {
		switch (person) {
		case "COLONEL_MOUTARDE":
			cluedoPlayerService.join(Person.COLONEL_MOUTARDE);
			break;
		case "DOCTEUR_OLIVE":
			cluedoPlayerService.join(Person.DOCTEUR_OLIVE);
			break;
		case "MADAME_LEBLANC":
			cluedoPlayerService.join(Person.MADAME_LEBLANC);
			break;
		case "MADAME_PERVANCHE":
			cluedoPlayerService.join(Person.MADAME_PERVANCHE);
			break;
		case "MADEMOISELLE_ROSE":
			cluedoPlayerService.join(Person.MADEMOISELLE_ROSE);
			break;
		case "PROFESSEUR_VIOLET":
			cluedoPlayerService.join(Person.PROFESSEUR_VIOLET);
			break;
		default:
			System.out.println("Le personnage " + person + " n'existe pas.");
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

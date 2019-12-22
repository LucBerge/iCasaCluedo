package fr.esisar.icasa.cluedo.player;

import org.apache.felix.ipojo.annotations.Component;

import fr.esisar.icasa.cluedo.common.CluedoPlateService;
import fr.esisar.icasa.cluedo.common.CluedoPlayerService;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Player;
import fr.liglab.adele.icasa.command.handler.CommandProvider;

@Component
@CommandProvider(namespace = "cluedo")
public class CluedoPlayer implements CluedoPlayerService {

	private Player me;

	/** Field for followMeConfiguration dependency */
	private CluedoPlateService cluedoPlateService;

	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("Stopping " + this.getClass().getName());
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Starting " + this.getClass().getName());
		join(Person.DOCTEUR_OLIVE);
		if (me == null) {
			join(Person.MADAME_LEBLANC);
			if (me == null) {
				join(Person.PROFESSEUR_VIOLET);
				if (me == null) {
					join(Person.MADEMOISELLE_ROSE);
				}
			}
		}
	}

	@Override
	public void join(Person person) {
		try {
			if (me == null) {
				me = cluedoPlateService.register(person);
				System.out.println("Vous vous êtes enregistré en tant que " + person.getName());
			} else {
				System.out.println("Vous êtes déjà enregistré en tant que " + person.getName());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public Player getPlayer() {
		return me;
	}
}

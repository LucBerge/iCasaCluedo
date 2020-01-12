package fr.esisar.icasa.cluedo.player;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;

import fr.esisar.icasa.cluedo.common.Person2;
import fr.esisar.icasa.cluedo.common.Player;
import fr.esisar.icasa.cluedo.plate.CluedoPlateService;
import fr.liglab.adele.icasa.command.handler.CommandProvider;

@Component
@Instantiate(name = "CluedoPlayer")
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
	}

	@Override
	public void join(Person2 person, String name) throws Exception {
		if (me != null)
			throw new Exception("Vous êtes déjà enregistré en tant que " + person.getName());
		me = cluedoPlateService.register(person, name);
	}

	@Override
	public Player getPlayer() {
		return me;
	}

	@Override
	public boolean AICanChoose() {
		return cluedoPlateService.AICanChoose();
	}

	@Override
	public boolean myTurn() {
		return cluedoPlateService.myTurn(me);
	}

	@Override
	public boolean isGameStarted() {
		return cluedoPlateService.isGameStarted();
	}
}

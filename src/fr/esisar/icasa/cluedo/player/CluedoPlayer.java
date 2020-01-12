package fr.esisar.icasa.cluedo.player;

import org.apache.felix.ipojo.annotations.Component;

import fr.esisar.icasa.cluedo.common.Crime;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Player;
import fr.esisar.icasa.cluedo.common.Supposition;
import fr.esisar.icasa.cluedo.plate.CluedoPlateService;
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
	}

	@Override
	public void join(Person person, String name) throws Exception {
		if (me != null)
			throw new Exception("Vous êtes déjà enregistré en tant que " + person.getName());
		me = cluedoPlateService.register(person, name);
	}

	@Override
	public void suppose(Crime supposition) throws Exception {
		cluedoPlateService.supposition(new Supposition(me, supposition));
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
	public boolean myTurn() throws Exception {
		return cluedoPlateService.myTurn(me);
	}

	@Override
	public boolean isGameStarted() {
		return cluedoPlateService.isGameStarted();
	}
}

package fr.esisar.icasa.cluedo.player;

import org.apache.felix.ipojo.annotations.Component;

import fr.esisar.icasa.cluedo.common.Crime;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Person2;
import fr.esisar.icasa.cluedo.common.Player;
import fr.esisar.icasa.cluedo.common.Room;
import fr.esisar.icasa.cluedo.common.Weapon;
import fr.esisar.icasa.cluedo.plate.CluedoPlateService;
import fr.liglab.adele.icasa.command.handler.CommandProvider;

@Component
@CommandProvider(namespace = "cluedo")
public class CluedoAI {

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
		play();
	}

	public void play() {
		//Wait for the player selection
		while (!cluedoPlateService.AICanChoose()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		//Selection
		try {
			me = cluedoPlateService.register(Person2.MADAME_LEBLANC, "AI");
		} catch (Exception e) {
			try {
				me = cluedoPlateService.register(Person2.MADAME_PERVANCHE, "AI");
			} catch (Exception e1) {
				try {
					me = cluedoPlateService.register(Person2.COLONEL_MOUTARDE, "AI");
				} catch (Exception e2) {
					try {
						me = cluedoPlateService.register(Person2.MADEMOISELLE_ROSE, "AI");
					} catch (Exception e3) {
						e3.printStackTrace();
					}
				}
			}
		}

		//Wait for the game to start
		while (!cluedoPlateService.isGameStarted()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		//While no finished
		while (cluedoPlateService.isGameStarted()) {

			//Wait for his turn
			while (!cluedoPlateService.myTurn(me)) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}

			//Make a supposition
			try {
				Crime supposition = new Crime(Person.COLONEL_MOUTARDE, Weapon.LAMPE, Room.BUREAU);
				cluedoPlateService.supposition(me, supposition);
			} catch (Exception e) {
			}
		}
	}
}

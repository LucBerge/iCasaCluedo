package fr.esisar.icasa.cluedo.player;

import java.util.ArrayList;
import java.util.List;

import org.apache.felix.ipojo.annotations.Component;

import fr.esisar.icasa.cluedo.common.Card;
import fr.esisar.icasa.cluedo.common.Clue;
import fr.esisar.icasa.cluedo.common.Crime;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Player;
import fr.esisar.icasa.cluedo.common.Room;
import fr.esisar.icasa.cluedo.common.Supposition;
import fr.esisar.icasa.cluedo.common.Weapon;
import fr.esisar.icasa.cluedo.plate.CluedoPlateService;
import fr.liglab.adele.icasa.command.handler.CommandProvider;

@Component
@CommandProvider(namespace = "cluedo")
public class CluedoAI {

	private final int SLEEP_TIME = 1000;

	private Player me;
	private Thread t;

	/** Field for followMeConfiguration dependency */
	private CluedoPlateService cluedoPlateService;

	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("Stopping " + this.getClass().getName());
		t.interrupt();
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Starting " + this.getClass().getName());
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					play();
				} catch (InterruptedException e) {
				}
			}
		});
		t.start();
	}

	private void play() throws InterruptedException {
		//Wait for the player selection
		while (!cluedoPlateService.AICanChoose()) {
			sleep();
		}

		//Selection
		int index = 1;
		for (Person person : Person.ALL) {
			try {
				me = cluedoPlateService.register(person, "IA " + index);
				break;
			} catch (Exception e) {
				index++;
			}
		}

		//If the AI can play
		if (me != null) {

			//Wait for the game to start
			while (!cluedoPlateService.isGameStarted()) {
				sleep();
			}

			try {
				//While not finished
				while (cluedoPlateService.isGameStarted()) {

					//Wait for his turn
					while (!cluedoPlateService.myTurn(me)) {
						sleep();
					}

					//Make a supposition
					try {
						cluedoPlateService.supposition(suppose());
					} catch (Exception e) {
					}
				}
			} catch (Exception e1) {
			}
		}
	}

	private Supposition suppose() {
		Person person;
		Weapon weapon;
		Room room;
		List<Card> known = new ArrayList<Card>();
		List<Clue> clues = cluedoPlateService.getClues();
		clues.forEach(c -> known.add(c.getCard()));

		do {
			person = Person.getRandom();
		} while (known.contains(person) || me.getCards().contains(person));
		do {
			weapon = Weapon.getRandom();
		} while (known.contains(weapon) || me.getCards().contains(weapon));
		do {
			room = Room.getRandom();
		} while (known.contains(room) || me.getCards().contains(room));

		return new Supposition(me, new Crime(person, weapon, room));
	}

	/** 
	 * Force the thread to sleep.
	 * @throws InterruptedException if the thread is interrupted.
	 */
	private void sleep() throws InterruptedException {
		Thread.sleep(SLEEP_TIME);
	}
}

package fr.esisar.icasa.cluedo.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.felix.ipojo.annotations.Component;

import fr.esisar.icasa.cluedo.common.Card;
import fr.esisar.icasa.cluedo.common.Crime;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Player;
import fr.esisar.icasa.cluedo.common.Room;
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

	public void play() throws InterruptedException {
		//Wait for the player selection
		while (!cluedoPlateService.AICanChoose()) {
			sleep();
		}

		//Selection
		for (Person person : Person.ALL) {
			try {
				me = cluedoPlateService.register(person, "AI");
				break;
			} catch (Exception e) {}
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
						//Init
						List<Card> suffledPersons = new ArrayList<Card>();
						List<Card> suffledWeapons = new ArrayList<Card>();
						List<Card> suffledRooms = new ArrayList<Card>();
	
						//Add
						suffledPersons.addAll(Arrays.asList(Person.ALL));
						suffledWeapons.addAll(Arrays.asList(Weapon.ALL));
						suffledRooms.addAll(Arrays.asList(Room.ALL));
	
						//Shuffle
						Collections.shuffle(suffledPersons);
						Collections.shuffle(suffledWeapons);
						Collections.shuffle(suffledRooms);
	
						//Pick 1 each
						Card person = suffledPersons.get(0);
						Card weapon = suffledWeapons.get(0);
						Card room = suffledRooms.get(0);
	
						Crime supposition = new Crime(person, weapon, room);
						cluedoPlateService.supposition(me, supposition);
					} catch (Exception e) {}
				}
			} catch (Exception e1) {}
		}
	}

	/** 
	 * Force the thread to sleep.
	 * @throws InterruptedException if the thread is interrupted.
	 */
	public void sleep() throws InterruptedException {
		Thread.sleep(SLEEP_TIME);
	}
}

package fr.esisar.icasa.cluedo.player;

import org.apache.felix.ipojo.annotations.Component;

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
				} catch (InterruptedException e) {}
			}
		});
		t.start();
	}

	public void play() throws InterruptedException {
		//Wait for the player selection
		while (!cluedoPlateService.AICanChoose()) {sleep();}

		//Selection
		for(Person person:Person.ALL) {
			try {
				me = cluedoPlateService.register(person, "AI");
			} catch (Exception e) {}
		}
		
		//If the AI can play
		if(me != null) {
			
			//Wait for the game to start
			while (!cluedoPlateService.isGameStarted()) {sleep();}

			//While no finished
			while (cluedoPlateService.isGameStarted()) {

				//Wait for his turn
				while (!cluedoPlateService.myTurn(me)) {sleep();}

				//Make a supposition
				try {
					Crime supposition = new Crime(Person.COLONEL_MOUTARDE, Weapon.LAMPE, Room.BUREAU);
					cluedoPlateService.supposition(me, supposition);
				} catch (Exception e) {
				}
			}
		}
	}
	
	public void sleep() throws InterruptedException {
		Thread.sleep(SLEEP_TIME);
	}
}

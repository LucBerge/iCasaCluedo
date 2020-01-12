package fr.esisar.icasa.cluedo.command;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;

import fr.esisar.icasa.cluedo.common.Crime;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Player;
import fr.esisar.icasa.cluedo.common.Room;
import fr.esisar.icasa.cluedo.common.Weapon;
import fr.esisar.icasa.cluedo.player.CluedoPlayerService;
import fr.liglab.adele.icasa.command.handler.Command;
import fr.liglab.adele.icasa.command.handler.CommandProvider;

@Component
@Instantiate(name = "CluedoPlayerCommand")
@CommandProvider(namespace = "cluedo")
public class CluedoPlayerCommand {

	/** Field for CluedoPlayerService dependency */
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
	public void join(String personName) {
		try {
			Person person = Person.fromName(personName);
			if(person != null)
				cluedoPlayerService.join(person, "Joueur");
			else
				System.out.println("Le personnage " + personName + " n'existe pas.");
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Make a supposition.
	 */
	@Command
	public void suppose(String personName, String weaponName, String roomName) {
		try {
			Person person = Person.fromName(personName);
			Weapon weapon = Weapon.fromSerialNumber(weaponName);
			Room room = Room.fromName(roomName);
			if(person != null && weapon != null && room != null) {
				Crime supposition = new Crime(person, weapon, room);
				cluedoPlayerService.suppose(supposition);
			}
			else {
				if(person == null)
					System.out.println("Le personnage " + personName + " n'existe pas.");
				if(weapon == null)
					System.out.println("L'arme " + weaponName + " n'existe pas.");
				if(room == null)
					System.out.println("La pièce " + roomName + " n'existe pas.");
			}
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

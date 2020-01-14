package fr.esisar.icasa.cluedo.plate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.felix.ipojo.annotations.Component;

import fr.esisar.icasa.cluedo.common.Card;
import fr.esisar.icasa.cluedo.common.Clue;
import fr.esisar.icasa.cluedo.common.Crime;
import fr.esisar.icasa.cluedo.common.Logger;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Player;
import fr.esisar.icasa.cluedo.common.Room;
import fr.esisar.icasa.cluedo.common.Supposition;
import fr.esisar.icasa.cluedo.common.Weapon;
import fr.liglab.adele.icasa.command.handler.CommandProvider;
import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.location.LocatedDevice;
import fr.liglab.adele.icasa.location.Position;
import fr.liglab.adele.icasa.service.location.PersonLocationService;
import fr.liglab.adele.icasa.simulator.listener.PersonListener;

@Component
@CommandProvider(namespace = "cluedo")
public class CluedoPlate implements DeviceListener, PersonListener, CluedoPlateService, CluedoCommandService {

	/**
	 * All the person cards in the game
	 */
	public static final Card[] CARDS_PERSONS = Person.ALL.clone();

	/**
	 * All the person cards in the game
	 */
	public static final Card[] CARDS_WEAPONS = Weapon.ALL.clone();

	/**
	 * All the person cards in the game
	 */
	public static final Card[] CARDS_ROOMS = Room.ALL.clone();

	/**
	 * Define if the game has started
	 */
	private boolean gameStarted = false;

	/** 
	* The number of players :
	**/
	private int numberOfPlayers = 4;

	/**
	 * The turn index.
	 */
	private int turn = -1;

	/** 
	* The commited crime :
	**/
	private Crime crime;

	/** PersonLocationService  dependency */
	private PersonLocationService personLocationService;

	/** Devices/Weapons on the plate dependency */
	private GenericDevice[] genericDevices;

	/** Field for persons dependency */
	private fr.liglab.adele.icasa.simulator.Person[] persons;

	/** Registered players*/
	private List<Player> players = new ArrayList<Player>();

	/** All known clues */
	private List<Clue> clues = new ArrayList<Clue>();

	private boolean fullAI = false;

	@Override
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	@Override
	public boolean isGameStarted() {
		return gameStarted;
	}

	@Override
	public void setNumberOfPlayers(int numberOfPlayers) throws Exception {
		if (gameStarted)
			throw new Exception("Le jeu a déjà débuté !");

		if (players.size() > 0)
			throw new Exception(players.size() + " joueurs ont déjà rejoind la partie.");

		this.numberOfPlayers = numberOfPlayers;

		if (players.size() == numberOfPlayers)
			shuffle();
	}

	/** Bind Method for presenceSensors dependency */
	public synchronized void bindGenericDevice(GenericDevice genericDevice, Map<String, String> properties) {
		System.out.println("Bind weapon " + genericDevice.getSerialNumber());
		genericDevice.addListener(this);
	}

	/** Unbind Method for presenceSensors dependency */
	public synchronized void unbindGenericDevice(GenericDevice genericDevice, Map<String, String> properties) {
		System.out.println("Unbind weapon " + genericDevice.getSerialNumber());
		genericDevice.removeListener(this);
	}

	/** Bind Method for persons dependency */
	public synchronized void bindPerson(fr.liglab.adele.icasa.simulator.Person person, Map<String, String> properties) {
		System.out.println("Bind person " + person.getName());
		person.addListener(this);
	}

	/** Unbind Method for persons dependency */
	public synchronized void unbindPerson(fr.liglab.adele.icasa.simulator.Person person,
			Map<String, String> properties) {
		System.out.println("Unbind person " + person.getName());
		person.removeListener(this);
	}

	/** Component Lifecycle Method */
	public synchronized void stop() {
		System.out.println("Stopping " + this.getClass().getName());
		for (GenericDevice genericDevice : genericDevices)
			genericDevice.removeListener(this);
	}

	/** Component Lifecycle Method */
	public synchronized void start() {
		System.out.println("Starting " + this.getClass().getName());
	}

	@Override
	public synchronized void reset() {
		if (gameStarted) {
			fullAI = false;
			players.clear();
			clues.clear();
			turn = -1;
			gameStarted = false;
		}
	}

	@Override
	public void devicePropertyModified(GenericDevice device, String propertyName, Object oldValue, Object newValue) {
		

		if (gameStarted) {
			List<GenericDevice> devices = getGenericDeviceFromLocation(
					device.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME).toString());
			Set<String> persons = personLocationService
					.getPersonInZone(device.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME).toString());

			if (devices.size() == 1 && persons.size() == 1) {
				Iterator<String> iterator = persons.iterator();
				Person person = Person.fromName(iterator.next());
				Weapon weapon = Weapon.fromSerialNumber(devices.get(0).getSerialNumber());
				Room room = Room.fromName(device.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME).toString());
				
				try {
					supposition(new Supposition(players.get(0), new Crime(person, weapon, room)));
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Return all GenericDevice from the given location
	 * 
	 * @param location - The given location
	 * @return The list of matching GenericDevice
	 */
	private synchronized List<GenericDevice> getGenericDeviceFromLocation(String location) {
		List<GenericDevice> binaryLightsLocation = new ArrayList<GenericDevice>();
		for (GenericDevice genericDevice : genericDevices) {
			if (genericDevice.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME).equals(location)) {
				binaryLightsLocation.add(genericDevice);
			}
		}
		return binaryLightsLocation;
	}

	@Override
	public synchronized Clue supposition(Supposition supposition) throws Exception {
		if (!players.get(turn).equals(supposition.getPlayer()))
			throw new Exception("Ce n'est pas à vous de jouer.");

		Clue clue = null;
		Logger.display(supposition.toString(), fullAI);

		if (crime.equals(supposition.getCrime())) {
			Logger.display(supposition.getPlayer().getName() + " a gagné !!!", fullAI);
			reset();
		} else {
			for (int i = 0; i < turn; i++) {
				clue = players.get(i).getClue(supposition.getCrime());
				if (clue != null) {
					break;
				}
			}
			if (clue == null) {
				for (int i = turn + 1; i < players.size(); i++) {
					clue = players.get(i).getClue(supposition.getCrime());
					if (clue != null) {
						break;
					}
				}
			}
		}

		if (turn >= players.size() - 1)
			turn = 0;
		else
			turn++;

		if (clue != null) {
			clues.add(clue);

			if (turn == 1 && !fullAI) {
				Logger.display(clue.getPlayer().getName() + " montre " + clue.getCard().getName(), fullAI);
			} else if (fullAI) {
				Logger.display(clue.toString(), fullAI);
			} else {
				Logger.display(clue.getPlayer().getName() + " montre une des cartes.", fullAI);
			}

		} else if (gameStarted)
			Logger.display("Personne ne possède ces cartes !", fullAI);

		return clue;
	}

	private void shuffle() {

		//Get a random crime
		crime = Crime.getRandom();
		System.out.println(crime);

		//Init
		List<Card> deck = new ArrayList<Card>();
		deck.addAll(Arrays.asList(CARDS_PERSONS));
		deck.addAll(Arrays.asList(CARDS_WEAPONS));
		deck.addAll(Arrays.asList(CARDS_ROOMS));

		deck.remove(crime.getPerson());
		deck.remove(crime.getWeapon());
		deck.remove(crime.getRoom());

		Collections.shuffle(deck);

		//Distribute
		int i = 0;
		for (Player player : players) {
			int j = 0;
			while (i + j < deck.size()) {
				player.getCards().add(deck.get(i + j));
				j += numberOfPlayers;
			}
			i++;
		}

		players.stream().forEach(p -> System.out.println(p));
		gameStarted = true;
		turn = 0;
	};

	@Override
	public synchronized Player register(Person person, String name) throws Exception {
		if (gameStarted)
			throw new Exception("Le jeu a déjà débuté !");

		if (players.stream().anyMatch(p -> p.getPerson().equals(person)))
			throw new Exception("Le personnage " + person.getName() + " est déjà utilisé.");

		Player player = new Player(person, name);
		players.add(player);

		if (players.size() == numberOfPlayers)
			shuffle();

		return player;
	}

	@Override
	public synchronized List<Clue> getClues() {
		return clues;
	}

	@Override
	public synchronized void setFullAI(boolean fullAI) {
		this.fullAI = fullAI;
	}

	@Override
	public synchronized boolean AICanChoose() {
		if (fullAI)
			return true;
		else
			return players.size() > 0;
	}

	@Override
	public synchronized boolean myTurn(Player player) throws Exception {
		if (turn == -1)
			throw new Exception("La partie est finie.");
		return players.get(turn).equals(player);
	}

	@Override
	public void deviceAdded(GenericDevice arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deviceEvent(GenericDevice arg0, Object arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void devicePropertyAdded(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void devicePropertyRemoved(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deviceRemoved(GenericDevice arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void personAdded(fr.liglab.adele.icasa.simulator.Person arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void personDeviceAttached(fr.liglab.adele.icasa.simulator.Person arg0, LocatedDevice arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void personDeviceDetached(fr.liglab.adele.icasa.simulator.Person arg0, LocatedDevice arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void personMoved(fr.liglab.adele.icasa.simulator.Person arg0, Position arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void personRemoved(fr.liglab.adele.icasa.simulator.Person arg0) {
		// TODO Auto-generated method stub
	}
}

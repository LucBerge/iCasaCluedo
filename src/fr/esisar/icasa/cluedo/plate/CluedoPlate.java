package fr.esisar.icasa.cluedo.plate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;

import fr.esisar.icasa.cluedo.common.Card;
import fr.esisar.icasa.cluedo.common.Clue;
import fr.esisar.icasa.cluedo.common.Crime;
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

	/** Devices/Weapons on the plate dependency */
	private GenericDevice[] genericDevices;
	/** Field for persons dependency */
	private fr.liglab.adele.icasa.simulator.Person[] persons;

	/** Registered players*/
	private List<Player> players = new ArrayList<Player>();

	/** All known clues */
	private List<Clue> clues = new ArrayList<Clue>();

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
			players.clear();
			clues.clear();
			gameStarted = false;
			turn = -1;
		}
	}

	@Override
	public void devicePropertyModified(GenericDevice device, String propertyName, Object oldValue, Object newValue) {
		if (gameStarted) {
			List<GenericDevice> devices = getGenericDeviceFromLocation(
					device.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME).toString());

			if (devices.size() == 1) {
				Weapon weapon = Weapon.fromSerialNumber(devices.get(0).getSerialNumber());
				Room room = Room.fromName(device.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME).toString());
				Person person = null;

				System.out.println(weapon);
				System.out.println(room);
				System.out.println(person);
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
		System.out.println(supposition);

		if (crime.equals(supposition.getCrime())) {
			System.out.println(supposition.getPlayer().getName() + " a gagné !!!");
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
			System.out.println(clue);
		} else if (gameStarted)
			System.out.println("Personne ne possède ces cartes !");

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
			throw new Exception("Personnage déjà utilisé.");

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
	public synchronized boolean AICanChoose() {
		return true;
		//return players.size() > 0;
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

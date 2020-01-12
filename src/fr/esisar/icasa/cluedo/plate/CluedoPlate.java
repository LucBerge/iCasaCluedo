package fr.esisar.icasa.cluedo.plate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;

import fr.esisar.icasa.cluedo.common.Card;
import fr.esisar.icasa.cluedo.common.Crime;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Person2;
import fr.esisar.icasa.cluedo.common.Player;
import fr.liglab.adele.icasa.command.handler.CommandProvider;
import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.location.LocatedDevice;
import fr.liglab.adele.icasa.location.Position;
import fr.liglab.adele.icasa.simulator.listener.PersonListener;

@Component
@Instantiate(name = "CluedoPlate")
@CommandProvider(namespace = "cluedo")
public class CluedoPlate implements DeviceListener, PersonListener, CluedoPlateService, CluedoCommandService {
	/**
	 * The name of the LOCATION property
	 */
	public static final String LOCATION_PROPERTY_NAME = "Location";

	/**
	 * The name of the location for unknown value
	 */
	public static final String LOCATION_UNKNOWN = "unknown";

	/**
	 * All the person cards in the game
	 */
	public static final Card[] CARDS_PERSONS = Person.ALL;

	/**
	 * All the person cards in the game
	 */
	public static final Card[] CARDS_WEAPONS = Person.ALL;

	/**
	 * All the person cards in the game
	 */
	public static final Card[] CARDS_ROOMS = Person.ALL;

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

		if (numberOfPlayers < players.size())
			throw new Exception(players.size() + " joueurs ont déjà rejoind la partie.");

		this.numberOfPlayers = numberOfPlayers;

		if (players.size() == numberOfPlayers)
			shuffle();
	}

	/** Bind Method for presenceSensors dependency */
	public synchronized void bindGenericDevice(GenericDevice genericDevice, Map properties) {
		System.out.println("Bind weapon " + genericDevice.getSerialNumber());
		genericDevice.addListener(this);
	}

	/** Unbind Method for presenceSensors dependency */
	public synchronized void unbindGenericDevice(GenericDevice genericDevice, Map properties) {
		System.out.println("Unbind weapon " + genericDevice.getSerialNumber());
		genericDevice.removeListener(this);
	}

	/** Bind Method for persons dependency */
	public synchronized void bindPerson(fr.liglab.adele.icasa.simulator.Person person, Map properties) {
		System.out.println("Bind person " + person.getName());
		person.addListener(this);
	}

	/** Unbind Method for persons dependency */
	public synchronized void unbindPerson(fr.liglab.adele.icasa.simulator.Person person, Map properties) {
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
			gameStarted = false;
			turn = -1;
		}
	}

	@Override
	public void devicePropertyModified(GenericDevice device, String propertyName, Object oldValue, Object newValue) {
		//		System.out.println(device.getPropertyValue(LOCATION_PROPERTY_NAME).toString());
		//		System.out.println(getGenericDeviceFromLocation(device.getPropertyValue(LOCATION_PROPERTY_NAME).toString()));
		System.out.println(persons.length);
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
			if (genericDevice.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				binaryLightsLocation.add(genericDevice);
			}
		}
		return binaryLightsLocation;
	}

	@Override
	public synchronized Card supposition(Player player, Crime supposition) throws Exception {
		if (!players.get(turn).equals(player))
			throw new Exception("Ce n'est pas à vous de jouer.");

		Card clue = null;
		System.out.println(player.getName() + " fait la suppose que " + supposition.getPerson() + " a tué avec "
				+ supposition.getWeapon() + " dans le/la " + supposition.getRoom());

		if (crime.getPerson().equals(supposition)) {
			System.out.println(player.getName() + "a gagné !!!");
			reset();
		} else {
			System.out.println("Donne une info !");
		}
		return clue;
	}

	private void shuffle() {

		//Init
		List<Card> suffledPersons = new ArrayList<Card>();
		List<Card> suffledWeapons = new ArrayList<Card>();
		List<Card> suffledRooms = new ArrayList<Card>();
		List<Card> shuffledCards = new ArrayList<Card>();

		//Add
		suffledPersons.addAll(Arrays.asList(CARDS_PERSONS));
		suffledWeapons.addAll(Arrays.asList(CARDS_WEAPONS));
		suffledRooms.addAll(Arrays.asList(CARDS_ROOMS));

		//Shuffle
		Collections.shuffle(suffledPersons);
		Collections.shuffle(suffledWeapons);
		Collections.shuffle(suffledRooms);

		//Pick 1 each
		Card person = suffledPersons.get(0);
		Card weapon = suffledWeapons.get(0);
		Card room = suffledRooms.get(0);
		crime = new Crime(person, weapon, room);
		suffledPersons.remove(0);
		suffledWeapons.remove(0);
		suffledRooms.remove(0);

		//Merge
		shuffledCards.addAll(suffledPersons);
		shuffledCards.addAll(suffledWeapons);
		shuffledCards.addAll(suffledRooms);

		//Shuffle all
		Collections.shuffle(shuffledCards);

		//Distribute
		int i = 0;
		for (Player player : players) {
			int j = 0;
			while (i + j < shuffledCards.size()) {
				player.getCards().add(shuffledCards.get(i + j));
				j += numberOfPlayers;
			}
			i++;
		}

		players.stream().forEach(p -> System.out.println(p));
		gameStarted = true;
		turn = 0;
	};

	@Override
	public synchronized Player register(Person2 person, String name) throws Exception {
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
	public synchronized boolean AICanChoose() {
		return players.size() > 0;
	}

	@Override
	public synchronized boolean myTurn(Player player) {
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

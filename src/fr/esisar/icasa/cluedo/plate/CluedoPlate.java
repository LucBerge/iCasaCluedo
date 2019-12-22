package fr.esisar.icasa.cluedo.plate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;

import fr.esisar.icasa.cluedo.common.CluedoCommandService;
import fr.esisar.icasa.cluedo.common.CluedoPlateService;
import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Player;
import fr.liglab.adele.icasa.command.handler.CommandProvider;
import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.location.LocatedDevice;
import fr.liglab.adele.icasa.location.Position;
import fr.liglab.adele.icasa.simulator.PersonType;
import fr.liglab.adele.icasa.simulator.listener.PersonListener;
import fr.liglab.adele.icasa.simulator.listener.PersonTypeListener;

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
	public static final String[] CARDS_PERSON = { Person.MADAME_LEBLANC.getName(), Person.MADAME_PERVANCHE.getName(),
			Person.COLONEL_MOUTARDE.getName(), Person.MADEMOISELLE_ROSE.getName(), Person.DOCTEUR_OLIVE.getName(),
			Person.PROFESSEUR_VIOLET.getName() };

	/**
	 * All the person cards in the game
	 */
	public static final String[] CARDS_WEAPONS = { "Tablette", "Lampe", "Radiateur", "Thermomètre",
			"Détecteur de présence", "Hautparleur" };

	/**
	 * All the person cards in the game
	 */
	public static final String[] CARDS_ROOMS = { "Conservatoire", "Cuisine", "Salle à manger", "Salle de bal", "Bureau",
			"Salle de billard", "Veranda", "Bibliothèque", "Hall" };

	/**
	 * Define if the game has started
	 */
	private boolean gameStarted = false;

	/** 
	* The number of players :
	**/
	private int numberOfPlayers = 4;

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

	private void shuffle() {
		gameStarted = true;

		//Init
		List<String> suffledPersons = new ArrayList<String>();
		List<String> suffledWeapons = new ArrayList<String>();
		List<String> suffledRooms = new ArrayList<String>();
		List<String> shuffledCards = new ArrayList<String>();

		//Add
		suffledPersons.addAll(Arrays.asList(CARDS_PERSON));
		suffledWeapons.addAll(Arrays.asList(CARDS_WEAPONS));
		suffledRooms.addAll(Arrays.asList(CARDS_ROOMS));

		//Shuffle
		Collections.shuffle(suffledPersons);
		Collections.shuffle(suffledWeapons);
		Collections.shuffle(suffledRooms);

		//Pick 1 each
		String person = suffledPersons.get(0);
		String weapon = suffledWeapons.get(0);
		String room = suffledRooms.get(0);
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

		players.stream().forEach(s -> System.out.println(s));
	};

	@Override
	public synchronized Player register(Person person) throws Exception {
		if (gameStarted)
			throw new Exception("Le jeu a déjà débuté !");

		if (players.stream().anyMatch(p -> p.getPerson().equals(person)))
			throw new Exception("Personnage déjà utilisé.");

		UUID uuid = UUID.randomUUID();
		Player player = new Player(uuid, person);
		players.add(player);

		if (players.size() == numberOfPlayers)
			shuffle();

		return player;
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

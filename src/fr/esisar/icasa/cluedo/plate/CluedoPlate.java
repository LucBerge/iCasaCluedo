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
import fr.esisar.icasa.cluedo.listener.PersonListener;
import fr.liglab.adele.icasa.command.handler.CommandProvider;
import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.service.location.PersonLocationService;

@SuppressWarnings("rawtypes")
@Component
@CommandProvider(namespace = "cluedo")
public class CluedoPlate implements DeviceListener, CluedoPlateService, CluedoCommandService {

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
	private int numberOfPlayers = 3;

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
	private PersonListener personListener;

	/** Registered players*/
	private List<Player> players = new ArrayList<Player>();

	/** All known clues */
	private List<Clue> clues = new ArrayList<Clue>();

	private boolean fullAI = false;

	public PersonLocationService getPersonLocationService() {
		return this.personLocationService;
	}

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

		if (players.size() > numberOfPlayers)
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

	/** Component Lifecycle Method */
	public synchronized void stop() {
		System.out.println("Stopping " + this.getClass().getName());
		for (GenericDevice genericDevice : genericDevices)
			genericDevice.removeListener(this);
		personListener.stop();
	}

	/** Component Lifecycle Method */
	public synchronized void start() {
		System.out.println("Starting " + this.getClass().getName());
		Logger.display("Deplacez un personnage dans une zone pour le jouer.", fullAI);
		personListener = new PersonListener(this);
		personListener.start();
	}

	@Override
	public synchronized void reset() {
		if (gameStarted) {
			fullAI = false;
			players.clear();
			clues.clear();
			turn = -1;
			gameStarted = false;
			Logger.display("Deplacez un personnage dans une zone pour le jouer.", fullAI);
		}
	}

	@Override
	public void devicePropertyModified(GenericDevice device, String propertyName, Object oldValue, Object newValue) {
		if (gameStarted) {
			Room room = Room.fromName(device.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME).toString());

			if (room != null) {
				List<Weapon> weapons = getWeaponsFromRoom(room);
				List<Person> persons = getPersonsFromRoom(room);

				if (weapons.size() == 1 && persons.size() == 1) {
					try {
						supposition(new Supposition(players.get(0), new Crime(persons.get(0), weapons.get(0), room)));
					} catch (Exception e) {
					}
				}
			}
		}
	}

	public void personLocationModified(Person person, Room room) {
		if (gameStarted) {
			List<Weapon> weapons = getWeaponsFromRoom(room);

			if (weapons.size() == 1) {
				try {
					supposition(new Supposition(players.get(0), new Crime(person, weapons.get(0), room)));
				} catch (Exception e) {
				}
			}
		} else {
			try {
				register(person, "Joueur");
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Return all the weapons from the given room
	 * 
	 * @param location - The given location
	 * @return The list of matching GenericDevice
	 */
	private synchronized List<Weapon> getWeaponsFromRoom(Room room) {
		List<Weapon> weapons = new ArrayList<Weapon>();
		for (GenericDevice genericDevice : genericDevices) {
			if (genericDevice.getPropertyValue(GenericDevice.LOCATION_PROPERTY_NAME).equals(room.getName())) {
				weapons.add(Weapon.fromSerialNumber(genericDevice.getSerialNumber()));
			}
		}
		return weapons;
	}

	/**
	 * Return all persons from the given room
	 * 
	 * @param location - The given location
	 * @return The list of matching Persons
	 */
	private synchronized List<Person> getPersonsFromRoom(Room room) {
		Set<String> personsName = personLocationService.getPersonInZone(room.getName());
		List<Person> persons = new ArrayList<Person>();
		Iterator<String> iterator = personsName.iterator();
		while (iterator.hasNext())
			persons.add(Person.fromName(iterator.next()));
		return persons;
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

		if (!fullAI) {
			StringBuilder builder = new StringBuilder("Voici vos cartes : ");
			for (Card card : players.get(0).getCards())
				builder.append(card.getName() + ", ");
			builder.delete(builder.length() - 2, builder.length());
			Logger.display(builder.toString(), fullAI);
		}
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
	public void deviceAdded(GenericDevice arg0) {}

	@Override
	public void deviceEvent(GenericDevice arg0, Object arg1) {}

	@Override
	public void devicePropertyAdded(GenericDevice arg0, String arg1) {}

	@Override
	public void devicePropertyRemoved(GenericDevice arg0, String arg1) {}

	@Override
	public void deviceRemoved(GenericDevice arg0) {}
}

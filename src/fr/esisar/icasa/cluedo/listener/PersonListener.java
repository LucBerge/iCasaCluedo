package fr.esisar.icasa.cluedo.listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fr.esisar.icasa.cluedo.common.Person;
import fr.esisar.icasa.cluedo.common.Room;
import fr.esisar.icasa.cluedo.plate.CluedoPlate;

public class PersonListener{
	
	private CluedoPlate plate;
	private Thread t;
	
	public PersonListener(CluedoPlate plate) {
		this.plate = plate;
	}
	
	public void start() {
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					listen();
				} catch (InterruptedException e) {
				}
			}
		});
		t.start();
	}
	
	public void listen() throws InterruptedException {
		Map<Person,Room> mapping = getCurrentMapping();
		
		while (true) {
			Map<Person,Room> currentMapping = getCurrentMapping();
			
			if(currentMapping.size() > mapping.size())
				sendDifference(mapping, currentMapping);
			
			mapping = currentMapping;
			Thread.sleep(500);
		}
	}
	
	public Map<Person,Room> getCurrentMapping() {
		Map<Person,Room> mapping = new HashMap<Person,Room>();
		
		for(Room room:Room.ALL) {
			Set<String> personsName = plate.getPersonLocationService().getPersonInZone(room.getName());
			Iterator<String> iterator = personsName.iterator();
			while(iterator.hasNext()) {
				mapping.put(Person.fromName(iterator.next()), room);
			}
		}
		return mapping;
	}
	
	public void sendDifference(Map<Person,Room> mapping, Map<Person,Room> currentMapping) {
		for(Map.Entry<Person, Room> entry : currentMapping.entrySet()) {
			if(!mapping.keySet().contains(entry.getKey()))
				plate.personLocationModified(entry.getKey(), entry.getValue());
		}
	}
	
	public void stop() {
		t.interrupt();
	}
}

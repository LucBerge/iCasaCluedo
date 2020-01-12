package fr.esisar.icasa.cluedo.command;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;

import fr.esisar.icasa.cluedo.plate.CluedoCommandService;
import fr.liglab.adele.icasa.command.handler.Command;
import fr.liglab.adele.icasa.command.handler.CommandProvider;

@Component
@Instantiate(name = "CluedoPlateCommand")
@CommandProvider(namespace = "cluedo")
public class CluedoPlateCommand {

	/** Field for CluedoCommandService dependency */
	@Requires
	private CluedoCommandService cluedoCommandService;

	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("Stopping " + this.getClass().getName());
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Starting " + this.getClass().getName());
	}

	@Command
	public void getNumberOfPlayers() {
		System.out.println(cluedoCommandService.getNumberOfPlayers());
	}
	
	@Command
	public void setNumberOfPlayers(String number) {
		try {
			cluedoCommandService.setNumberOfPlayers(Integer.parseInt(number));
		}catch(Exception e) {
			System.out.println(number + " is not a number.");
		}
	}
	
	@Command
	public void reset() {
		cluedoCommandService.reset();
	}
}

package fr.esisar.icasa.cluedo.common;

import java.io.IOException;

import javax.swing.JFrame;

public class Logger{
	
	public JFrame frame;

	public static void display(String text, boolean fullAI) {
		if(!fullAI) {
			String message[] = {"java", "-jar", "/home/lucas/Logger.jar", text};
			
			try {
				Runtime re = Runtime.getRuntime();
				Process command = re.exec(message);
		        command.waitFor();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}else {
			System.out.println(text);
		}
	}
}

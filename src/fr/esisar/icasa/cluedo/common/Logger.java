package fr.esisar.icasa.cluedo.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Logger{
	
	public JFrame frame;
	private String text;
	
	public static void display(String text, boolean fullAI) {
		if(!fullAI) 
			display(text);
		else 
			System.out.println(text);
	}
	
	public static void display(String...text) {
		StringBuilder builder = new StringBuilder();
		builder.append("<html>");
		for(int i=0; i<text.length; i++) {
			builder.append(text[i]);
			builder.append("<br>");
		}
		builder.append("</html>");
		
		Logger attente = new Logger(builder.toString());
		attente.frame.setVisible(true);
	}
	
	private Logger(String text) {
		this.text = text;
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				frame.dispose();
			}
		});
		frame.setUndecorated(true);
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);		
		frame.setOpacity(0.75f);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
			
		JLabel text = new JLabel(this.text);
		text.setFont(new Font("Tahoma", Font.PLAIN, 100));
		text.setBounds(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
		text.setForeground(Color.BLACK);
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setVerticalAlignment(SwingConstants.CENTER);
		frame.add(text);  
	}
}

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

	public static void display(String text) {
		Logger attente = new Logger(text);
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

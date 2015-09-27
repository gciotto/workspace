import java.awt.*;

import javax.swing.*;


public class JResultat extends JPanel {

	private JTextArea coups;
	private JButton button;
	
	public JResultat () {
		
		super();
		
		coups = new JTextArea();
		coups.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(coups);
		
		JLabel label = new JLabel ("Liste de Coups:");
		
		button = new JButton ("Prochain Coup");
		button.setEnabled(false);
		
		this.setLayout(new BorderLayout());
		
		
		this.add(label, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(button, BorderLayout.SOUTH);
		
		
	}
	
}

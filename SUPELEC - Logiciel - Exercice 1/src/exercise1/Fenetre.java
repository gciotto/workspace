package exercise1;

import java.awt.Dimension;
import javax.swing.JFrame;

public class Fenetre {

	public static void main(String[] args) {
		
		JFrame fenetre = new JFrame("Exercice 1: Une nouvelle fenetre");
		
		MonDessin carre = new MonDessin();
		
		Dimension size = new Dimension (600, 600);
		fenetre.setPreferredSize(size);
		fenetre.add(carre);
		fenetre.pack();
		fenetre.setVisible(true);

	}

}

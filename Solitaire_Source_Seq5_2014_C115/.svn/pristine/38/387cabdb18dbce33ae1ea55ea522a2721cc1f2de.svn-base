package interfaces;

import java.awt.*;

import javax.swing.*;
import entites.*;

public class JCase extends JPanel{

	/**
	 * 
	 */
	private int etat;

	public JCase () {
		super();
		this.etat = Case.PAS_EN_USE;
	}
	
	public JCase (int e) {
		super();
		this.etat = e;
	}

	public void paint(Graphics g) {

		super.paint(g);
		
		Dimension d = this.getSize();

		Color c = Color.LIGHT_GRAY;

		switch (this.etat) {

		case Case.OCCUPEE 		: c = Color.BLACK; break;
		case Case.VIDE			: c = Color.WHITE; break;
		case Case.PAS_EN_USE 	: c = Color.LIGHT_GRAY; break;

		}

		g.setColor(c);

		g.fillOval(0, 0,((int) (0.95*d.getWidth())),((int) (0.95*d.getHeight())));

	}
	public int getEtat() {
		
		return etat; 
	}

	public void changeEtat(int nEtat) {
		
		this.etat = nEtat;
	}


}

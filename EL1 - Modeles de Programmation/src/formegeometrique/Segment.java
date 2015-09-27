/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package formegeometrique;

import structure.Vector;
import interaction.Painter;

public class Segment extends GraphicalElement {

	private Point origine, fin;

	public Segment(Point origine, Point fin) {
		this.origine = origine;
		this.fin = fin;
	}

	public void draw(Painter painter) {

		double 	longueur_x 	= this.fin.getX() - this.origine.getX(),
				longueur_y 	= this.fin.getY() - this.origine.getY(),
				longueur   	= Math.max( Math.abs(longueur_x), Math.abs(longueur_y) ),
				incr_x		= longueur_x / longueur,
				incr_y		= longueur_y / longueur,
				x			= this.origine.getX() + 0.5,
				y			= this.origine.getY() + 0.5;
		
		for (int i = 1; i < longueur; i++) {
				painter.setPixel( (int) x, (int) y, true);
				x += incr_x;
				y += incr_y;
		}
	}

	public void translate(Vector vect) {

		this.origine.translate(vect);
		this.fin.translate(vect);
	}

	public void rotate(double angle) {
		
		this.origine.rotate(angle);
		this.fin.rotate(angle);		
	}
}

/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package formegeometrique;

import structure.Vector;
import interaction.Painter;

public abstract class GraphicalElement {

	public abstract void draw (Painter painter);
	public abstract void translate(Vector vect);
	public abstract void rotate(double angle);
	
	/**
	 * I)   Translation de -v
	 * II)  Rotation autour de l'origine
	 * III) Translation de v
	 */
	public void rotate(Point centre, double angle) {
		
		this.translate(centre.getPoint().opposite());
		this.rotate(angle);
		this.translate(centre.getPoint());
		
	}
	
}

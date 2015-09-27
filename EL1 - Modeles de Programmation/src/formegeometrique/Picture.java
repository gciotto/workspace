/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package formegeometrique;

import structure.Vector;
import interaction.Painter;

public class Picture {

	/*** AVANT GENERALISATION
	private Point[] points;
	private Segment[] segments;
	 **/

	private GraphicalElement [] elements;	/* APRES GENERALISATION */
	private Painter painter;

	public Picture(int height, int width, GraphicalElement[] elements) {

		this.elements = elements;
		this.painter = new Painter(width, height);
	}

	public void display () {

		this.painter.clear();

		for (int i = 0; i < this.elements.length; i++)
			this.elements[i].draw(painter);

	}

	public void translateElements (Vector vect) {

		for (int i = 0; i < this.elements.length; i++)
			this.elements[i].translate(vect);

	}

	public void rotateElements(double angle) {
		
		for (int i = 0; i < this.elements.length; i++)
			this.elements[i].rotate(angle);
	}
	
	public void rotateElements(Point centre, double angle) {
		
		for (int i = 0; i < this.elements.length; i++)
			this.elements[i].rotate(centre, angle);
	}
	
	
}

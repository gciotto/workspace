/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package formegeometrique;

import structure.Vector;
import interaction.Painter;

public class Polygon extends GraphicalElement {

	protected Point[] points;
	protected int taille;	
	
	public Polygon(Point[] points) {
		this.points = points;
		this.taille = points.length;
	}
	
	public int getSize() {
		return this.taille;
	}

	public void draw(Painter painter) {
		
		for (int i = 0; i < this.taille; i++) {
			Segment aux = 
					new Segment(this.points[i], this.points[(i+1) % taille]);
			aux.draw(painter);
		}

	}

	public void translate(Vector vect) {

		for (int i = 0; i < this.taille; i++)
			this.points[i].translate(vect);
		
	}

	public void rotate(double angle) {

		for (int i = 0; i < this.taille; i++)
			this.points[i].rotate(angle);
		
	}

}

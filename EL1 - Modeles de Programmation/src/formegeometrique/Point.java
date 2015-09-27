/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package formegeometrique;

import structure.Matrix;
import structure.Vector;
import interaction.Painter;

public class Point extends GraphicalElement {

	private Vector point;
	
	public Point(double x, double y) {
		
		this.point = new Vector (new double [] {x, y});
	}
	
	public double getX() {
		return this.point.get(0);
	}
	
	public double getY() {
		return this.point.get(1);
	}
	
	public Vector getPoint() {
		return point;
	}

	public void draw(Painter painter) {
		
		painter.setPixel( (int) this.getX(), (int) this.getY(), true);
	}

	public void translate(Vector vect) {
		
		
		for (int i = 0; i < this.point.getSize(); i++) 
			this.point.set(i, this.point.get(i) + vect.get(i));
		
		
		//this.point = this.point.add(vect);
		
	}

	public void rotate(double angle) {
		
		Matrix aux = new Matrix (
						new double [][]{	
								{ Math.cos(angle), -Math.sin(angle) },
								{ Math.sin(angle),  Math.cos(angle) } });
		
		this.point = aux.multiply(this.point);
		
	}
	
}

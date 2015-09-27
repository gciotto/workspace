/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package formegeometrique;

public class KochSnowFlake extends Polygon {

	public KochSnowFlake(Point[] points) {
		super(points);
	}

	private int signal (Point v1, Point v2) {
		
		double 	delta_x 	= v2.getX() - v1.getX(), 
				delta_y 	= v2.getY() - v1.getY();
		
		if (delta_x > 0 && delta_y > 0)
			return 1;
		
		if (delta_x < 0 && delta_y > 0)
			return 1;
		
		if (delta_x > 0 && delta_y < 0)
			return -1;
		
		return -1;
	}
	
	private Point[] kochTransformAnEdge(Point v1, Point v2) {
		
		Point[] resultat = new Point[5];
		
		resultat[0] = v1;
		resultat[4] = v2;
		
		
		double 	delta_x 	= v2.getX() - v1.getX(), 
				delta_y 	= v2.getY() - v1.getY(),
				longueur 	= Math.sqrt( delta_x*delta_x + delta_y*delta_y ) / 3,
				x_mid 		= (v2.getX() + v1.getX()) / 2,
				y_mid		= (v2.getY() + v1.getY()) / 2,
				h			= Math.sqrt(3)*longueur / 2;
		
		resultat [1] = new Point (v1.getX() + delta_x/3, 
									v1.getY() + delta_y / 3 ); 
		
		resultat [3] = new Point (v1.getX() + 2*delta_x/3, 
									v1.getY() + 2*delta_y / 3 );
		
		double 	aux_x, aux_y;
		
		if ( delta_x != 0 && delta_y != 0 ) {
			
			/* coefficient de la droite perpendicular entre le point 
			 * au milieu du segment et le nouveau point   */
			double 	coefficient = - delta_x / delta_y;
			
			/*
			 * Equations utilisees: 
			 * ( y - ym ) = ( x - xm ) * m_perpendicular
			 * ( y - ym )^2 + ( x - xm )^2 = sqrt(3)/2 * h
			 * 
			 * l = longueur
			 * (l/2)^2 + h^2 = l^2 --> h = sqrt(3)/2 * l
			 * 
			 */
			
			aux_x = signal(v1, v2) * h / Math.sqrt (1.0 + coefficient * coefficient)
						+ x_mid;
			
			aux_y = (aux_x - x_mid)*coefficient + y_mid;
			
		} else if (delta_x == 0) {
			
			aux_y = y_mid;
			
			if (delta_y  > 0)
				aux_x = x_mid + h;
			else aux_x = x_mid - h;
			
		} else {
			
			aux_x = x_mid;
			
			if (delta_x  > 0)
				aux_y = y_mid - h;
			else aux_y = y_mid + h;
			
		}
		
		resultat[2] = new Point(aux_x, aux_y);
		
		return resultat;
	}
	
	public void kochTransform() {
		
		Point [] 	resultat = new Point[4*this.getSize()],
					aux;
		
		int actuel = 0;
		
		for (int i = 0; i < this.getSize(); i++) {
			
			resultat [actuel++] = this.points[i];
			aux = kochTransformAnEdge(this.points[i],
							this.points[(i+1)% this.taille]);
			
			for (int j = 1; j < 4; j++) 
				resultat [actuel++] = aux[j];
		}
		
		this.points = resultat;
		this.taille = resultat.length;
	}
}

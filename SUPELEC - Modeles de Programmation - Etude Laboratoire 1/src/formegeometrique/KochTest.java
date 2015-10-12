/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package formegeometrique;

import interaction.Painter;

public class KochTest {

	public static void main(String[] args) {
		
		final Point[] tri = new Point[] { 
		        new Point(350, 300), 
		        new Point(550, 300), 
		        new Point(450, 441) };
		 
		KochSnowFlake snowflake = new KochSnowFlake(tri);
		 
		GraphicalElement[] elements = { snowflake };
		 
		Picture pict = new Picture(600, 600, elements);
		 
		Point centre = new Point (300, 300);
		
		for (int depth = 0; depth < 10; depth++) {
		    
			pict.display();
		    snowflake.kochTransform();
		    
		    pict.rotateElements(centre, Math.PI/2);
		    
		    Painter.delay(2000);
		}

	}

}

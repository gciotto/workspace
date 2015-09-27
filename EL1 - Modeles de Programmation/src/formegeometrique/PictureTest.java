/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package formegeometrique;

import structure.Vector;
import interaction.Painter;

public class PictureTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Point p1 = new Point(5, 5);
		Point p2 = new Point(10, 5);
		Point p3 = new Point(5, 10);
		 		
		Segment s1 = new Segment(new Point(20, 5), new Point(20, 105)); // segment vertical de 100 pixels
		 
		Segment s2 = new Segment(new Point(150, 125), new Point(180, 140)); // segment oblique, à peu près au centre d'une fenêtre 300*300

		/*Picture picture0 = new Picture(300, 300,
								new GraphicalElement[] {p1, p2, p3, s1,s2});
		picture0.display(); */
		
		GraphicalElement[] elements = new GraphicalElement[9];
		 
	    Point[] square = new Point[4];
	    square[0] = new Point(315, 355);
	    square[1] = new Point(315, 300);
	    square[2] = new Point(370, 300);
	    square[3] = new Point(370, 355);
	 
	    Point[] star = new Point[5];
	    star[0] = new Point(420, 245);
	    star[1] = new Point(460, 355);
	    star[2] = new Point(490, 245);
	    star[3] = new Point(400, 315);
	    star[4] = new Point(515, 315);
	 
	    elements[0] = new Segment(new Point(100, 245), new Point(110, 355));
	    elements[1] = new Segment(new Point(110, 355), new Point(145, 255));
	    elements[2] = new Segment(new Point(145, 255), new Point(180, 355));
	    elements[3] = new Segment(new Point(180, 355), new Point(190, 245));
	    elements[4] = new Segment(new Point(230, 355), new Point(230, 245));
	    elements[5] = new Segment(new Point(230, 245), new Point(285, 245));
	    elements[6] = new Segment(new Point(315, 355), new Point(315, 245));
	 
	    elements[7] = new Polygon(square);
	    elements[8] = new Polygon(star);
	 
	    Picture pict = new Picture(600, 600, elements);
	 
	    pict.display();
	    
	    Point centre = new Point(300, 300);
	    
	    for (int i = 0; i < 5; i++) {
	    	pict.translateElements(new Vector(new double[] {10, 10}));
	    	pict.display();
	    	Painter.delay(10);
	    }
		
	    while (true) {
	        Painter.delay(10);
	        pict.rotateElements(centre, 0.02);
	        pict.display();
	    }
	}

}

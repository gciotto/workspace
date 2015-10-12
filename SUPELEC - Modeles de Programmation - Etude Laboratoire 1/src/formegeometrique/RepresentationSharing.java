/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package formegeometrique;

import interaction.Painter;
import structure.Vector;

public class RepresentationSharing {
    public static void main(String[] args) {
        Point p1 = new Point(20, 150);
        Point p2 = new Point(20, 280);
        Point p3 = new Point(20, 20);
         
        Segment seg1 = new Segment(p1, p2);
        Segment seg2 = new Segment(p1, p3);
         
        GraphicalElement[] elements = {seg1, seg2};
         
        Picture picture = new Picture(400, 300, elements);
         
        Vector translationVector = new Vector(new double[] {1, 0});
         
        for(int i=0; i<180; i++) {
            picture.display();
            picture.translateElements(translationVector);
            Painter.delay();
        }
    }
}

/**
*	
*	Le point p1 se move plus vite parce qu'il souffre deux translations,
*	  pendant les autres points qui composent les segments souffrent simplement une.  
*
*	La figure se move horizontallement parce que la coordonnée de translation
*	   y est nule.
*
*/

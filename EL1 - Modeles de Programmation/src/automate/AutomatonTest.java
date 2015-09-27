/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package automate;

import interaction.Painter;

public class AutomatonTest {

	public static void main (String args[]) {
		
		Painter painter = new Painter(300,100);
		Painter painter2 = new Painter(300,100);
		Painter painter3 = new Painter(300,100);
		
		GameOfLife gl = new GameOfLife(300, 100);
		DayNight dn = new DayNight(300, 100);
		Highlife hl = new Highlife(300, 100);
		
		while (true) {
			gl.draw(painter);
			gl.oneStep();
			
			dn.draw(painter2);
			dn.oneStep();
			
			hl.draw(painter3);
			hl.oneStep();
			
			Painter.delay(100);
		}
	}
}

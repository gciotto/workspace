import interfaces.*;
import controleurs.*;

public class Solitaire {

	/**
	 * 
	 */
	public static void main(String[] args) { 
		JSolitaire cIO = new JSolitaire ();
		InterfaceFichier iF = new InterfaceFichier();
		ControleurJeu c = new ControleurJeu(iF, cIO);
		
		cIO.changeControleur(c);
		c.commencerJeu();
	 } 

}
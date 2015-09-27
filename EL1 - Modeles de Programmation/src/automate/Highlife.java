/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/

package automate;

public class Highlife extends AbstractAutomaton {

	public Highlife(int colonnes, int lignes) {
		super(colonnes, lignes);
	}

	
	/*
	 *  Naissance si 3 ou 6 voisines,
		Survie si 2 ou 3 voisines.
		
	 * @see automate.AbstractAutomaton#getNextState(boolean, int)
	 */
	public boolean getNextState(boolean currentState, int neighborCount) {
		
		if (!currentState && (neighborCount == 3 || neighborCount == 6 ) )
			return true;
		
		if (currentState && (neighborCount == 2 || neighborCount == 3))
			return true;
		
		return false;
	}

}

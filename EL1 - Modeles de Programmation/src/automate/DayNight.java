/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package automate;

public class DayNight extends AbstractAutomaton {

	public DayNight(int colonnes, int lignes) {
		super(colonnes, lignes);
	}

	/*
	 * 
    Naissance d'une cellule (a partir d'une morte) si elle est entourée de 3, 6, 7 ou 8 voisines.
    Une cellule vivante survit si elle est entourée de 3, 4, 6, 7 ou 8 cellules voisines vivantes.

	 * @see automate.AbstractAutomaton#getNextState(boolean, int)
	 */
	
	public boolean getNextState(boolean currentState, int neighborCount) {

		if (neighborCount == 3 || neighborCount >= 6)
			return true;
		
		if (currentState && neighborCount == 4)
			return true;
		
		return false;
	}

}

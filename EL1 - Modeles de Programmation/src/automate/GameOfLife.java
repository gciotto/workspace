/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package automate;

public class GameOfLife extends AbstractAutomaton {

	public GameOfLife(int colonnes, int lignes) {
		super(colonnes, lignes);
	}

	public boolean getNextState(boolean currentState, int neighborCount) {
		
		if (!currentState && neighborCount == 3)
			return true;
		
		if (currentState && neighborCount <= 1)
			return false;
		
		if (currentState && neighborCount <= 3)
			return true;
		
		return false;
	}
	
}

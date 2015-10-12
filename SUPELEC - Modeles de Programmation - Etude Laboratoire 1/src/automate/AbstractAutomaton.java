/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package automate;

import interaction.Painter;

public abstract class AbstractAutomaton {

		private boolean[][] monde;
		private int colonnes, lignes;

		/**
		 * Methode constructor qui construit un nouveau monde aléatoirement.
		 * 
		 * @param colonnes Le nombre de colonnes du monde
		 * @param lignes Le nombre de lignes du monde
		 */
		public AbstractAutomaton(int colonnes, int lignes) {
			monde = new boolean[colonnes][lignes];
			this.colonnes = colonnes;
			this.lignes = lignes;

			for (int x = 0; x < this.colonnes; x++) {

				for (int y = 0; y < this.lignes; y++) {
					
					/* si moins que 50%, la respective cellule est morte */
					if (Math.random() < 0.5)  
						monde[x][y] = false;
					else monde[x][y] = true;
				}
			}

		}

		public abstract boolean getNextState(boolean currentState, int neighborCount);
		
		public void draw(Painter painter) {

			/* parcourt la matrice  */
			for (int x = 0; x < this.colonnes; x++) 
				for (int y = 0; y < this.lignes; y++)
					painter.setPixel(x, y, this.monde[x][y]);
		}

		
		public int countNeighbors(int column, int row) {
			
			int resultat = 0;
			
			/*  parcourt tous les voisins du point */
			for (int x = column - 1; x <= column + 1; x++) {
				if (x >= 0 && x < this.colonnes) {
					
					for (int y = row - 1; y <= row + 1; y++){ 
					
						if (y >= 0 && y < this.lignes) {
							if ( !(y == row && x == column) ) {
								if ( monde[x][y] )
									resultat++;
							}					
						}
					}
				}
			}
			
			return resultat;
			
		}
		
		public void oneStep() {
			
			boolean[][] aux = new boolean [this.colonnes][this.lignes];
			
			for (int x = 0; x < this.colonnes; x++) 
				for (int y = 0; y < this.lignes; y++)
					aux[x][y] = this.getNextState(monde[x][y], countNeighbors(x,y));		
			
			monde = aux;
		}	
}

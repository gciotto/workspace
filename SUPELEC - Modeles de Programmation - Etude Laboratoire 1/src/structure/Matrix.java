/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package structure;

public class Matrix {

	private double[][] coefficients;
	private int ligne, colonne;

	public Matrix(double[][] coefficients) {
		this.coefficients = coefficients;
		
		this.ligne = this.coefficients.length;
		this.colonne = this.coefficients[0].length;
	}
	
	public String toString() {
		
		String resultat = "[";
		
		for (int i = 0; i < this.ligne; i++) {
			
			resultat += " [";
			
			for (int j = 0; j < this.colonne; j++ ) {
				
				resultat += this.coefficients[i][j];
				
				if (j != this.colonne - 1)
					resultat += ", ";				
			}
			
			resultat += "]";
			if (i != this.ligne - 1)
				resultat += ";";
			
		}
		
		resultat += " ]";
		
		return resultat;
	}
	
	public int getLigne() {
		return ligne;
	}

	public int getColonne() {
		return colonne;
	}

	public double get (int i, int j) {
		return this.coefficients[i][j];
	}
	
	public void set (int i, int j, double valeur) {
		this.coefficients[i][j] = valeur;
	}
	
	public Matrix add(Matrix other) {
		
		int 	otherLigne 	= other.getLigne(),
				otherColonne = other.getColonne(); 
		
		if (this.ligne != otherLigne ||
				this.colonne != otherColonne )
			throw new Error("Les deux matrices ont des dimensions differentes");
		
		Matrix resultat = new Matrix (
				new double[otherLigne][otherColonne]);
		
		for (int i = 0; i < this.ligne; i++) 
			for (int j = 0; j < this.colonne; j++)
				resultat.set(i, j, other.get(i, j) + this.coefficients[i][j]);
		
		return resultat;
	}
	
	public Vector multiply(Vector vector) {
		
		if ( this.colonne != vector.getSize() )
			throw new Error("La matrice et le tableau ont des dimensions differentes");
		
		Vector resultat = new Vector ( new double [this.ligne] );
				
		for (int i = 0; i < this.ligne; i++) {
			
			double aux = 0;
			for (int j = 0; j < this.colonne; j++) 
				aux += this.coefficients[i][j] * vector.get(j);
			
			resultat.set(i, aux);
		}
		
		return resultat;
	}
}

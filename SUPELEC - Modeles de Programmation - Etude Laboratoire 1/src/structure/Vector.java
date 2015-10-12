/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package structure;

public class Vector {

	private double[] coefficients;
	private int taille;

	public Vector(double[] coefficients) {
		this.coefficients = coefficients;
		this.taille = coefficients.length;
	}

	public int getSize() {
		return this.taille;
	}

	public double get (int index) {
		return this.coefficients[index];
	}

	public void set (int i, double valeur) {
		this.coefficients[i] = valeur;
	}

	public String toString() {

		String resultat = "< ";

		for (int i = 0; i < this.taille; i++) {

			resultat += this.coefficients[i];
			if (i != this.taille - 1)
				resultat += ", ";

		}

		resultat += " >";

		return resultat;
	}

	public Vector add(Vector other) {

		if (this.taille != other.getSize() )
			throw new Error("Les deux tableaux ont des dimensions differentes");

		Vector resultat = new Vector ( new double [this.taille] );

		for (int i = 0; i < this.taille; i++) 
			resultat.set(i, this.coefficients[i] + other.get(i));

		return resultat;
	}

	public Vector subtract(Vector other) {

		if (this.taille != other.getSize() )
			throw new Error("Les deux tableaux ont des dimensions differentes");

		Vector resultat = new Vector ( new double [this.taille] );

		for (int i = 0; i < this.taille; i++) 
			resultat.set(i, this.coefficients[i] - other.get(i));

		return resultat;
	}
		
	public Vector opposite() {
		
		Vector resultat = new Vector ( new double [this.taille]);
		
		for (int i = 0; i < this.taille; i++) 
			resultat.set(i, -this.coefficients[i]);
		
		return resultat;
	}
}

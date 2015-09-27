
public class Monomial {
	
	Rational coefficient;
	int degre;
	
	public Monomial(Rational coefficient, int degre) {
	
		this.coefficient = coefficient;
		this.degre = degre;
	}
	
	public String toString(){
		
		String resultat = "";
		
		if ( this.coefficient.isOne() ) {
			
			if (this.degre == 0)
				return Integer.toString(1);
			
			if (this.degre == 1)
				return "X";
			
			return "X^" + this.degre;
			
		}
		
		if (this.degre == 0)
			return this.coefficient.toString();
		
		if (this.degre == 1)
			return this.coefficient + "*X";
			
		
		return this.coefficient+"*X^"+this.degre;
	}
	
	Rational evaluate(Rational x) {
		
		Rational resultat = new Rational(1,1);
		
		for (int i = 0; i < this.degre; i++)
			resultat = resultat.multiply(x);
		
		return resultat.multiply(this.coefficient);
		
	}

}

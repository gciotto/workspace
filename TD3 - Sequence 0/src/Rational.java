
public class Rational {
	
	int denominateur, numerateur;

	Rational(int numerateur, int denominateur) {
		
		this.denominateur = denominateur;
		this.numerateur = numerateur;
	}

	public boolean isOne() {
		return (this.numerateur ==
				this.denominateur);
	}

	public String toString() {
		
		
		if (this.denominateur == 1)
			return Integer.toString(this.numerateur); 
		
		if (this.numerateur == 0)
			return Integer.toString(0);
		
		return this.numerateur + "/" + this.denominateur;
	}

	
	Rational add(Rational other) {
		
		Rational resultat = new Rational 
			(other.numerateur * this.denominateur + other.denominateur * this.numerateur,
					other.denominateur * this.denominateur);
		
		return resultat.simplify();
		
	}
	
	Rational multiply(Rational other) {
		
		Rational resultat = new Rational 
			(other.numerateur * this.numerateur,
					other.denominateur * this.denominateur);
		
		return resultat.simplify();
		
	}
	
	static int gcd (int a, int b) {
		
		if (a == b) return a;
		
		if (a < b) return gcd (a, b - a); 
		
		return gcd (a - b, b);
	}
		
	
	Rational simplify() {
		
		int maximum = gcd(this.denominateur, this.numerateur);
		
		return new Rational (this.numerateur / maximum, 
								this.denominateur / maximum);
		
	}
	
	
	double approximate() {
		return (double) this.numerateur / this.denominateur;
	}
	
	
}

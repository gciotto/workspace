
public class Polynome {
	
	Monomial[] monomes;

	public Polynome(Monomial[] monomes) {
		
		this.monomes = monomes;
	}
	
	int getDegree(){
		
		if (monomes == null) return 0;
		
		int degree = monomes[0].degre;
		
		for (int i = 1; i < monomes.length; i++) 
			if (degree < monomes[i].degre) {
				degree = monomes[i].degre;
			}
		
		return degree;
	}
	
	public String toString() {
		
		String resultat = "";
		
		for (int i = 0; i < this.monomes.length; i++) {
				
			if (i > 0)
				resultat += " + ";
			
			resultat += this.monomes[i];
		}
	
		return resultat;
	}
	
	Rational evaluate(Rational x) {
		
		Rational resultat = this.monomes[0].evaluate(x);
		
		for (int i = 1; i < this.monomes.length; i++)
			resultat = resultat.add(this.monomes[i].evaluate(x));
		
		return resultat;
		
	}
	
	double approximate(Rational x) {
		return this.evaluate(x).approximate();
	}
	
	static void swap (Monomial [] array, int iOrigine, int iDestination) {
		
		Monomial temp = array[iOrigine];
		array[iOrigine] = array[iDestination];
		array[iDestination] = temp;		
		
	}

	Polynome canonize() {
		
		Monomial[] aux = new Monomial[this.monomes.length];
		
							
		for (int i = 0; i < aux.length; i++) {
			Rational r = this.monomes[i].coefficient;
			
			aux[i] = new Monomial
					( new Rational(r.numerateur, r.denominateur)
					, this.monomes[i].degre);
		}
		
		
		for (int i = 0; i < aux.length; i++) {
			
			for (int j = i + 1; j < aux.length; j++) {
				if (aux[i].degre < aux[j].degre) {
					swap(aux, i, j);
				}
			}
			
		}
		
		int i = 0,
			taille = 0;
				
		while (i < aux.length) {
			
			int j = i + 1;
				
			while (	j < aux.length 
					&& aux[i].degre == aux[j].degre) {
				
				Rational 	r = aux[i].coefficient,
							s = aux[j].coefficient;
				
				aux[i].coefficient = r.add(s);
				
				j++;				
			}
			
			taille++;
			i = j;
		}
		
		Polynome resultat = new Polynome(new Monomial[taille]);
		
		int iResultat = 0;
		i = 0;
		
		while (i < aux.length) {
			
			int j = i + 1;
				
			while (	j < aux.length 
						&& aux[i].degre == aux[j].degre) {
				
				j++;
			}
			
			resultat.monomes[iResultat] = aux[i];
			
			iResultat++;
			i = j;
		}
				 
		return resultat;
	}
	
}

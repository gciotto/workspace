package entites;

public class Case {

	private int etat;
	private Coordonnee coordonnee;

	public final static int VIDE		= 0;
	public final static int OCCUPEE	 	= 1;
	public final static int PAS_EN_USE 	= 2;
	
	public int getEtat() {
		return etat; 
	}


	public Coordonnee getCoordonnee() {
		return coordonnee; 
	}

	public Case(int etat, Coordonnee c) { 
		this.etat = etat;
		this.coordonnee = new Coordonnee (c.getLigne(), c.getColonne());
	}
	
	public Case(int etat, int ligne, int col) { 
		this.etat = etat;
		this.coordonnee = new Coordonnee (ligne, col);
	}

	public void changeEtat(int nouveauEtat) { 

		this.etat = nouveauEtat;
	}
	
	public String toString () {
		
		String resultat;
		
		switch (this.etat) {
		
			case OCCUPEE 	: resultat = "1"; break;
			case VIDE 		: resultat = "0"; break;
			case PAS_EN_USE : resultat = "#"; break;
			default: resultat = ""; 
		
		}
		
		return resultat;
	}

}

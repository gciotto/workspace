package entites;

import java.util.*;

public class Plateau {

	private Case[][] cases;
	private List<Case> listeCasesVides;
	private int piecesRestantes; 
	
	public Case[][] getCases() {
		return cases; 
	}

	public Plateau(int nbLigne, int nbColonne) { 

		cases = new Case[nbLigne][nbColonne];
		
		for (int i = 0; i < nbLigne; i++)
			for (int j = 0; j < nbColonne; j++) 
				cases [i][j] = new Case(Case.PAS_EN_USE, i, j);
		
		this.listeCasesVides = new ArrayList<Case>();
		this.piecesRestantes = 0;
	}

	public void changeEtatA(Coordonnee c, int nEtat) { 
		
		this.changeEtatA(c.getLigne(), c.getColonne(), nEtat);
		
	}
	
	public void changeEtatA(int row, int col, int nEtat) { 
		
		Case cs = cases[row][col];
		
		if (nEtat == Case.VIDE && cs.getEtat() != Case.VIDE) {
			
			if (cs.getEtat() == Case.OCCUPEE)
				this.piecesRestantes--;
			
			this.listeCasesVides.add(cs);
		}
		
		if (cs.getEtat() == Case.VIDE && nEtat != Case.VIDE ) {
			
			if (nEtat == Case.OCCUPEE)
				this.piecesRestantes++;
			
			this.listeCasesVides.remove(cs);
		}
		
		if (cs.getEtat() == Case.PAS_EN_USE && nEtat == Case.OCCUPEE)
			this.piecesRestantes++;
		
		if (cs.getEtat() == Case.OCCUPEE && nEtat == Case.PAS_EN_USE)
			this.piecesRestantes--;
			
		cs.changeEtat(nEtat);
	}	
	
	public int getPiecesRestantes() {
		return piecesRestantes;
	}

	public int getEtatA(Coordonnee c) { 
		
		return cases[c.getLigne()][c.getColonne()].getEtat();
	}

	public String toString() { 
		
		String resultat = "";
		for (int i = 0; i < this.cases.length; i++) {
			for (int j = 0; j < this.cases[i].length; j++) {
				resultat += cases[i][j].toString() + " ";
			}
			
			resultat += "\n";
		}
		
		return resultat;
	}
	
	public int getNbLignes () {
		return this.cases.length;
	}
	
	public int getNbColonnes() {
		if (this.cases[0] != null)
			return this.cases[0].length;
		
		return 0;
	}
	
	public List<Case> getCasesVides() {

		return this.listeCasesVides;
	}
	
	public Case getCaseA(int r, int c) {
		
		if (r < this.getNbLignes() && r >= 0)
			if (c < this.getNbColonnes() && c >= 0)
				return cases[r][c];
		return null;
	}
	
	
	
	
	public List<Case> getVoisinesA(int r, int c) {
		
		List<Case> voisines = new ArrayList<Case>();
		
		int nbColonnes = this.getNbColonnes(),
			nbLignes = this.getNbLignes();
		
		for (int i = r - 1; i <= r+1 ; i++) 
			for (int j = c - 1; j <= c+1; j++) {
				
				if (i != r || j != c) 
					if (i >= 0 && i < nbLignes)
						if (j >= 0 && j < nbColonnes)
							if (this.getCaseA(i, j).getEtat() == Case.OCCUPEE)
								voisines.add(this.getCaseA(i, j));
			}
		
		/*
		if (c + 1 < nbColonnes && this.getCaseA(r, c + 1).getEtat() == Case.OCCUPEE) //DROITE
			voisines.add(this.getCaseA(r, c + 1));
		
		if (c - 1 >= 0 && this.getCaseA(r, c - 1).getEtat() == Case.OCCUPEE) //GAUCHE
			voisines.add(this.getCaseA(r, c - 1));
		
		if (r - 1 >= 0 && this.getCaseA(r - 1, c).getEtat() == Case.OCCUPEE) //HAUTE
			voisines.add(this.getCaseA(r - 1, c));
		
		if (r + 1 < nbLignes && this.getCaseA(r + 1, c).getEtat() == Case.OCCUPEE) //BAS
			voisines.add(this.getCaseA(r + 1, c));
		
		if (r + 1 < nbLignes && c + 1 < nbColonnes && this.getCaseA(r + 1, c + 1).getEtat() == Case.OCCUPEE) //BAS
			voisines.add(this.getCaseA(r + 1, c + 1));
		
		if (r - 1 >= 0 && c - 1 >= 0 && this.getCaseA(r - 1, c - 1).getEtat() == Case.OCCUPEE) //BAS
			voisines.add(this.getCaseA(r - 1, c - 1));
		
		if (r + 1 < nbLignes && c - 1 >= 0 && this.getCaseA(r + 1, c - 1).getEtat() == Case.OCCUPEE) //BAS
			voisines.add(this.getCaseA(r + 1, c - 1));
		
		*/
		
		
		return voisines;		
	}

	public void actualiserPlateau(Coup c) {
				
			this.changeEtatA(c.getCoordonneeOrigine(), Case.VIDE);
			
			this.changeEtatA(c.getCoordonneeEliminee(), Case.VIDE);
			
			this.changeEtatA(c.getCoordonneeCible(), Case.OCCUPEE);
			
	}

	public void annuleCoup(Coup cand) {
		
		Coordonnee cAncienne = cand.getCoordonneeOrigine();
		
		this.changeEtatA(cAncienne, Case.OCCUPEE);
		
		this.changeEtatA(cand.getCoordonneeCible(), Case.VIDE);
		
		this.changeEtatA(cand.getCoordonneeEliminee(), Case.OCCUPEE);
		
	}

	public  List<Case>  getVoisinesA(Coordonnee cible) {
		return this.getVoisinesA(cible.getLigne(), cible.getColonne());
	}
	
	public Coordonnee getCentre() {
		return new Coordonnee (this.getNbLignes() / 2, this.getNbColonnes() / 2);
	}

	public int getDistanceDeA(Coordonnee origine, Coordonnee cible) {
	
		return (origine.getLigne() - cible.getLigne())*(origine.getLigne() - cible.getLigne()) +
				(origine.getColonne() - cible.getColonne())*(origine.getColonne() - cible.getColonne());
	}

	public void eliminerCoups(List<Coup> resultatPremier) {
		
		int taille = resultatPremier.size();
		
		while (taille > 0) {
			this.annuleCoup(resultatPremier.get(taille-1));
			taille--;
		}

	}
	
}

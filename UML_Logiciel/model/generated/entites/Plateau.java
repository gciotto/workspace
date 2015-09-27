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
		/*
		List<Case> resultat = new ArrayList<Case>();
		
		for (int i = 0; i < this.cases.length; i++)
			for (int j = 0; j < this.cases[i].length; j++) {
				
				if (this.cases[i][j].getEtat() == Case.VIDE)
					resultat.add(cases[i][j]);
			}
		
		return resultat;
		*/
		
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
		
		if (r - 1 >= 0 && this.getCaseA(r - 1, c).getEtat() == Case.OCCUPEE) //HAUTE
			voisines.add(this.getCaseA(r - 1, c));
		if (r + 1 < this.getNbLignes() && this.getCaseA(r + 1, c).getEtat() == Case.OCCUPEE) //BAS
			voisines.add(this.getCaseA(r + 1, c));
		if (c - 1 >= 0 && this.getCaseA(r, c - 1).getEtat() == Case.OCCUPEE) //GAUCHE
			voisines.add(this.getCaseA(r, c - 1));
		if (c + 1 < this.getNbColonnes() && this.getCaseA(r, c + 1).getEtat() == Case.OCCUPEE) //DROITE
			voisines.add(this.getCaseA(r, c + 1));
		
		
		
		return voisines;		
	}

	public void actualiserPlateau(Coup c) {
	
			int lOrigine = c.getCoordonneeOrigine().getLigne(),
				cOrigine = c.getCoordonneeOrigine().getColonne(),
				lNouvelle = 0, cNouvelle = 0;
			
			this.changeEtatA(c.getCoordonneeOrigine(), Case.VIDE);
			
			switch (c.getDirection().getDirectionCode()) {
			
			case Direction.BAS 		: 	lOrigine++;
										lNouvelle = lOrigine + 1;
										cNouvelle = cOrigine;
										break;
			case Direction.HAUTE 	: 	lOrigine--;
										lNouvelle = lOrigine - 1;
										cNouvelle = cOrigine;
										break;
			case Direction.DROITE	: 	cOrigine++;
										cNouvelle = cOrigine + 1;
										lNouvelle = lOrigine;
										break;
			case Direction.GAUCHE	: 	cOrigine--;
										cNouvelle = cOrigine - 1;
										lNouvelle = lOrigine;
										break;		
			}
			
			this.changeEtatA(lNouvelle, cNouvelle, Case.OCCUPEE);
			this.changeEtatA(lOrigine, cOrigine, Case.VIDE);
			
	}

	public void annuleCoup(Coup cand, int eAncien) {
		
		Coordonnee cAncienne = cand.getCoordonneeOrigine();
		
		this.changeEtatA(cAncienne, eAncien);
		
		int l = cAncienne.getLigne(),
			c = cAncienne.getColonne();
		
		switch (cand.getDirection().getDirectionCode()) {
		
		case Direction.HAUTE: 
			this.changeEtatA(l - 1, c, Case.OCCUPEE);
			this.changeEtatA(l - 2, c, Case.VIDE);
			break;
			
		case Direction.BAS:
			this.changeEtatA(l + 1, c, Case.OCCUPEE);
			this.changeEtatA(l + 2, c, Case.VIDE);
			break;

		case Direction.DROITE:
			this.changeEtatA(l, c + 1, Case.OCCUPEE);
			this.changeEtatA(l, c + 2, Case.VIDE);
			break;
			
		case Direction.GAUCHE:
			this.changeEtatA(l, c - 1, Case.OCCUPEE);
			this.changeEtatA(l, c - 2, Case.VIDE);
			break;
		}
		
		
	}

}

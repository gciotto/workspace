package controleurs;

import entites.*;

import java.io.*;
import java.util.*;

public class ControleurJeu {

	private List<Coup> listeCoups;
	private Plateau plateau;
	private IControleurIO controleurIO;
	private IControleurInterface controleurInterface;
	private int movements;

	public ControleurJeu(IControleurIO cIO, IControleurInterface cInterface) { 

		this.controleurIO = cIO;
		this.controleurInterface = cInterface;
		this.listeCoups = new ArrayList<Coup>();		
		this.movements = 0;
	}

	public List<Coup> getListeCoups() {
		return listeCoups; 
	}

	public Plateau getPlateau() {
		return plateau; 
	}

	public IControleurIO getControleurIO() {
		return controleurIO; 
	}

	public IControleurInterface getControleurInterface() {
		return controleurInterface; 
	}

	public Plateau lirePlateau(IMillieu m) throws FileNotFoundException, IOException { 

		this.plateau = this.controleurIO.lirePlateau(m);
		this.listeCoups.clear();
		this.movements = 0;		
		return this.plateau;
	}

	public boolean ecrireListeCoups(IMillieu m) throws IOException { 
		return this.controleurIO.ecritListeCoup(this.listeCoups, m);
	}

	public List<Coup> coupsPossiblesMaxCoups() { 

		List<Coup> coups = new ArrayList<Coup>(), coups_2 = new ArrayList<Coup>();
		int nbPlusGrande = 0, nbPlusGrande_2 = 0 , nbActuel;

		for (Coup c : this.coupsPossibles()) {

			this.plateau.actualiserPlateau(c);

			nbActuel = this.coupsPossibles().size();

			if (nbActuel > nbPlusGrande) {

				coups_2.addAll(coups);
				nbPlusGrande_2 = nbPlusGrande;

				coups.clear();
				coups.add(c);

				nbPlusGrande = nbActuel;
			}
			else if (nbActuel == nbPlusGrande)
				coups.add(c);
			else if (nbActuel > nbPlusGrande_2) {

				coups_2.clear();
				coups_2.add(c);

				nbPlusGrande_2 = nbActuel;
			}
			else if (nbActuel == nbPlusGrande_2)
				coups_2.add(c);

			this.plateau.annuleCoup(c);

		}

		coups.addAll(coups_2);

		return coups;
	}


	public List<Coup> coupsPossibles() { 

		List<Coup> coups = new ArrayList<Coup>();

		for (Case c : this.plateau.getCasesVides()) {

			int x = c.getCoordonnee().getLigne(),
					y = c.getCoordonnee().getColonne();

			for (Case v : this.plateau.getVoisinesA(x, y)) {

				int col = v.getCoordonnee().getColonne(),
						row = v.getCoordonnee().getLigne();

				if (v.getEtat() == Case.OCCUPEE && this.isValid(row, col, x, y)) {

					if (col == y - 1 ) { // DROITE

						Case candidat = this.plateau.getCaseA(x, y - 2);
						if (candidat != null && candidat.getEtat() == Case.OCCUPEE)
							coups.add( new Coup (new Coordonnee (x, y - 2), new Direction (Direction.DROITE)));

					}

					if (row == x + 1 ) { // HAUTE

						Case candidat = this.plateau.getCaseA(x + 2, y);
						if (candidat != null && candidat.getEtat() == Case.OCCUPEE)
							coups.add( new Coup (new Coordonnee (x + 2, y), new Direction (Direction.HAUT)));

					}
					
					if (col == y + 1 ) { // GAUCHE

						Case candidat = this.plateau.getCaseA(x, y + 2);
						if (candidat != null && candidat.getEtat() == Case.OCCUPEE)
							coups.add( new Coup (new Coordonnee (x, y + 2), new Direction (Direction.GAUCHE)));

					}

					if (row  == x - 1 ) { // BAS

						Case candidat = this.plateau.getCaseA(x - 2, y);
						if (candidat != null && candidat.getEtat() == Case.OCCUPEE)
							coups.add( new Coup (new Coordonnee (x - 2, y), new Direction (Direction.BAS)));

					}

				}
			}

		}

		return coups;
	}

	private boolean isValid(int row, int col, int x, int y) {
		return (row == x && col == y - 1) || (row == x && col == y + 1) ||
				(row == x + 1 && col == y) || (row == x - 1 && col == y);
	}

	public Coup prochainCoup() { 

		List<Coup> listePossibles = this.coupsPossibles();

		if (listePossibles.isEmpty())
			return null;

		Coup prochain = listePossibles.get(0);

		return prochain;
	}

	public Coup prochainDistanceMaxVoisinCoup() { 

		List<Coup> listePossibles = this.coupsPossibles();

		if (listePossibles.isEmpty())
			return null;

		int choisi = 0, total = 0;
		double dist = 0;

		Coup coupAvant; 

		if (this.listeCoups.isEmpty())
			coupAvant = new Coup(new Coordonnee (this.plateau.getNbLignes() / 2,this.plateau.getNbColonnes() / 2), 
					new Direction(1));
		else coupAvant = this.listeCoups.get(this.listeCoups.size() - 1);

		Coup prochain = null;		

		for (Coup cand : listePossibles) {

			int distAux = this.plateau.getDistanceDeA(cand.getCoordonneeOrigine(), 
					coupAvant.getCoordonneeOrigine());

			if (distAux > dist) {
				choisi = total;
				dist = distAux;
			}

			total++;
		}		

		prochain = listePossibles.get(choisi);

		return prochain;
	}

	public void ajouterCoup(Coup c) {

		this.listeCoups.add(c);

		this.plateau.actualiserPlateau(c);

		this.movements++;

	}

	public Coup prochainDistanceMinVoisinCoup() { 

		List<Coup> listePossibles = this.coupsPossibles();

		if (listePossibles.isEmpty())
			return null;

		int choisi = 0, total = 0;
		int dist = this.plateau.getNbColonnes()*this.plateau.getNbColonnes() + 
				this.plateau.getNbLignes()*this.plateau.getNbLignes(); 

		Coup coupAvant = new Coup(new Coordonnee (0,0), 
											new Direction(1));


		Coup prochain = null;		

		for (Coup cand : listePossibles) {

			int distAux = this.plateau.getDistanceDeA(cand.getCoordonneeOrigine(), coupAvant.getCoordonneeOrigine());

			if (distAux < dist) {
				choisi = total;
				dist = distAux;
			}

			total++;
		}		

		prochain = listePossibles.get(choisi);

		return prochain;
	}



	public List<Coup> prochainNiveauBranchAndBound(List<Coup> precedent, int niveau) {

		if (niveau <= 0)
			return this.prochaineListeMaxCoups();

		System.out.println("Niveau "+niveau);
		
		List<Coup> 	resultat = null,
				resultatAux,
				possibles = this.coupsPossiblesMaxCoups();

		int nbCoups = 0, nbCoupsPossibles = possibles.size(), i = 1;


		for (Coup c: possibles) {

			resultatAux = new ArrayList<Coup>();
			resultatAux.add(c);
			this.plateau.actualiserPlateau(c);

			precedent.add(c);

			double temps = System.currentTimeMillis(), total;

			List <Coup> resultatProchain = this.prochainNiveauBranchAndBound(precedent, niveau - 1);

			precedent.remove(c);

			total = System.currentTimeMillis() - temps;

			if (resultatProchain !=null)
				resultatAux.addAll(resultatProchain);

			if (niveau >= 1) 
				System.out.println("Niveau "+niveau+ " ( "+ i +" / "+ nbCoupsPossibles + ") - Temps: " + (total/1000) +"s");
			i++;

			this.plateau.annuleCoup(c);

			if (resultatAux.size() > nbCoups) {

				if (niveau <= 5 ) {

					List<Coup> cocozao = new ArrayList<Coup>();
					cocozao.addAll(precedent);
					cocozao.addAll(resultatAux);

					try {
						Fichier f = new Fichier(new File("/home/gciotto/Desktop/TESTE/board10_concours.cps."+cocozao.size()));
						System.out.println("Niveau "+niveau+" Ecriture de /home/gciotto/Desktop/TESTE/board10_concours.cps."+cocozao.size());
						this.controleurIO.ecritListeCoup(cocozao, f);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				resultat = resultatAux;
				nbCoups = resultatAux.size();
			}
		}			

		return resultat;

	}

	public Coup prochainMaxCoup() { 

		List<Coup> listePossibles = this.coupsPossiblesMaxCoups();

		if (listePossibles.isEmpty())
			return null;

		int choisi = 0, nbCoupsMax = 0, total = 0;

		Coup prochain = null;		

		for (Coup cand : listePossibles) {

			int nbCoups;

			this.plateau.actualiserPlateau(cand);

			nbCoups = this.coupsPossibles().size(); 

			this.plateau.annuleCoup(cand);

			if (nbCoups > nbCoupsMax) {

				choisi = total;
				nbCoupsMax = nbCoups;				
			}

			total++;
		}		

		prochain = listePossibles.get(choisi);

		return prochain;
	}


	public Coup prochainMinVoisinCoup() { 

		List<Coup> listePossibles = this.coupsPossibles();

		if (listePossibles.isEmpty())
			return null;

		int choisi = 0, nbVoisins = 9, total = 0;
		Coup prochain = null;		

		for (Coup cand : listePossibles) {

			int nbVoisinsCaseVide;

			Coordonnee cible = cand.getCoordonneeCible();

			nbVoisinsCaseVide = this.plateau.getVoisinesA(cible).size(); 

			if (nbVoisinsCaseVide + 1 < nbVoisins) {

				choisi = total;
				nbVoisins = nbVoisinsCaseVide;				
			}


			total++;
		}		

		prochain = listePossibles.get(choisi);

		return prochain;
	}


	public List<Coup> prochaineListeMaxCoups() {

		List<Coup> resultat = new ArrayList<Coup> ();

		Coup c = this.prochainMaxCoup();

		while (c != null) {

			this.plateau.actualiserPlateau(c);

			resultat.add(c);	

			c = this.prochainMaxCoup();
		}

		this.plateau.eliminerCoups(resultat);

		return resultat;

	}


	public List<Coup> prochaineListeMinDistance() {

		List<Coup> resultat = new ArrayList<Coup> ();

		Coup c = this.prochainDistanceMinVoisinCoup();

		while (c != null) {

			this.plateau.actualiserPlateau(c);

			resultat.add(c);	

			c = this.prochainDistanceMinVoisinCoup();
		}

		this.plateau.eliminerCoups(resultat);

		return resultat;

	}


	public List<Coup> prochaineListeMaxVoisinage() {

		List<Coup> resultat = new ArrayList<Coup> ();

		Coup c = this.prochainMaxVoisinCoup();

		while (c != null) {

			this.plateau.actualiserPlateau(c);

			resultat.add(c);	

			c = this.prochainMaxVoisinCoup();
		}

		this.plateau.eliminerCoups(resultat);

		return resultat;

	}

	public List<Coup> prochaineListeDeRandomCoups() {


		List<Coup> 	resultat = new ArrayList<Coup>();
		Coup aux = this.prochainRandomCoup();

		while (aux != null) {

			this.plateau.actualiserPlateau(aux);
			resultat.add(aux);

			aux = this.prochainRandomCoup();
		}

		this.plateau.eliminerCoups(resultat);

		return resultat;

	}


	public List<Coup> prochaineListePlusieursBranchBound() {

		int total = 10, meilleureTaille = 0;

		List<Coup> 	aux = this.prochaineBranchBoundList(),
				resultat = null;

		while (total > 0) {

			if (aux.size() > meilleureTaille ) {

				resultat = aux;
				meilleureTaille = aux.size();
			}

			aux = this.prochaineBranchBoundList();
			total--;
		}

		return resultat;

	}


	public List<Coup> prochaineBranchBoundList() {

		List<Coup> 	resultatAux,
		listePossibles = this.coupsPossiblesMaxCoups(),
		resultatPremier = new ArrayList<Coup>(),
		resultat;

		if (listePossibles.isEmpty())
			return null;

		Coup prochain = listePossibles.remove(0);
		while (prochain != null) {

			this.plateau.actualiserPlateau(prochain);
			resultatPremier.add(prochain);
			prochain = this.prochainRandomCoup();
		}

		int taillePremiere = resultatPremier.size();

		this.plateau.eliminerCoups(resultatPremier);

		if (!resultatPremier.isEmpty())
			resultat = resultatPremier;
		else return null;

		for (Coup c : listePossibles) {

			resultatAux = new ArrayList<Coup>();

			while (c != null) {

				this.plateau.actualiserPlateau(c);
				resultatAux.add(c);
				c = this.prochainRandomCoup();
			}

			this.plateau.eliminerCoups(resultatAux);

			int tailleAux = resultatAux.size();

			if (tailleAux > taillePremiere) {

				resultat = resultatAux;
				taillePremiere = tailleAux;
			}


		}

		return resultat;
	}


	public List<Coup> prochaineListeMelangee(){

		List<Coup> resultat = new ArrayList<Coup>();

		List<Coup> cand = this.prochaineBranchBoundList();

		while (this.plateau.getPiecesRestantes() > 25 && !cand.isEmpty()) {

			Coup c = cand.remove(0);
			resultat.add(c);
			this.plateau.actualiserPlateau(c);

		}

		resultat.addAll(this.prochaineListeOptimale(false, new BooleanResult(false)));

		this.plateau.eliminerCoups(resultat);

		return resultat;
	}


	public List<Coup> prochaineListeOptimale(boolean estPremier, BooleanResult aTrouve) { 

		List<Coup> listePossibles = this.coupsPossibles();

		List<Coup> resultat = new ArrayList<Coup>();

		int count = 0;

		while (!listePossibles.isEmpty() && !aTrouve.getResult()) {

			Coup cand = listePossibles.remove(0);
			List<Coup> resultatAux = new ArrayList<Coup>();

			this.plateau.actualiserPlateau(cand);

			int restantes = this.plateau.getPiecesRestantes();

			resultatAux.add(cand);

			if (restantes  <= 1) { 
				aTrouve.setResult(true);
				this.plateau.annuleCoup(cand);
				return resultatAux;

			} else {

				resultatAux.addAll(this.prochaineListeOptimale(false, aTrouve));
				this.plateau.annuleCoup(cand);
			}

			if (resultatAux.size() > resultat.size())
				resultat = resultatAux;

			count++;

			if (estPremier) listePossibles.clear();
		}

		return resultat;
	}


	public Coup prochainMaxVoisinCoup() { 

		List<Coup> listePossibles = this.coupsPossibles();

		if (listePossibles.isEmpty())
			return null;

		int choisi = 0, nbVoisins = 0, total = 0;
		Coup prochain = null;		

		for (Coup cand : listePossibles) {

			int nbVoisinsCaseVide;

			Coordonnee cible = cand.getCoordonneeCible();

			nbVoisinsCaseVide = this.plateau.getVoisinesA(cible).size(); 

			if (nbVoisinsCaseVide - 1 > nbVoisins) {

				choisi = total;
				nbVoisins = nbVoisinsCaseVide;				
			}


			total++;
		}		

		prochain = listePossibles.get(choisi);

		return prochain;
	}

	public Coup prochainCoupVersLeCentre() {

		List<Coup> listePossibles = this.coupsPossibles();

		if (listePossibles.isEmpty())
			return null;

		Coordonnee centre = this.plateau.getCentre();

		int choisiIdeal = listePossibles.size(),l = this.plateau.getNbLignes(), c = this.plateau.getNbColonnes(),
				total = 0, dist = this.plateau.getDistanceDeA(new Coordonnee(0,0), centre);

		Coup prochain = null;		

		boolean ok = false;

		for (Coup cand : listePossibles) {

			Coordonnee 	cible = cand.getCoordonneeCible(),
					origine = cand.getCoordonneeEliminee();


			int distanceOrigineCentre = this.plateau.getDistanceDeA(origine, centre),
					distanceCibleCentre = this.plateau.getDistanceDeA(cible, centre),
					lig = cand.getCoordonneeOrigine().getLigne(),
					col = cand.getCoordonneeOrigine().getColonne();

			if ( !( lig - 1 == 0  || lig + 1 == l || col - 1 == 0 || col + 1 == c) 
					|| coupsPossibles().size() == 1 || 
					(total == listePossibles.size() - 1 && choisiIdeal == listePossibles.size())){

				if ( (col == c ) || (col == 0) || (lig == 0) || (l == lig)) {
					choisiIdeal = total;
					ok = true;
				}
				else 
					if (distanceCibleCentre < dist && !ok)  { 
						choisiIdeal = total;
						dist = distanceCibleCentre; 
					}
			}
			total++;
		}		

		prochain = listePossibles.get(choisiIdeal);

		return prochain;

	}

	public Coup prochainRandomCoup() { 

		List<Coup> listePossibles = this.coupsPossiblesMaxCoups();

		if (listePossibles.isEmpty())
			return null;

		Random randomGenerator = new Random();

		int indice = 0; 

		if (!listePossibles.isEmpty()) 
			indice = randomGenerator.nextInt(listePossibles.size());

		Coup prochain = listePossibles.get(indice);

		return prochain;
	}

	public void commencerJeu() { 
		this.controleurInterface.metEnOperation();
	} 

	public static Plateau plateauVide() {

		Plateau p = new Plateau(9, 9);

		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				p.changeEtatA(new Coordonnee(i,j), Case.PAS_EN_USE);

		return p;
	}

	public List<Case> getListeCasesVides() {
		return this.plateau.getCasesVides();
	}

	public int getTotalMovements() {
		return this.movements;
	}

	public int getPiecesRestantes() {
		return this.plateau.getPiecesRestantes();
	}

	public List <Coup> lireCoups(Fichier f) throws IOException {

		List <Coup> coups = this.controleurIO.lireCoups(f);

		for (Coup c : coups) 
			this.ajouterCoup(c);

		return coups;
	}
}

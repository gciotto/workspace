package interfaces;

import entites.*;
import controleurs.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import entites.Coup;

public class InterfaceFichier implements IControleurIO {

	public boolean ecritPlateau(Plateau plateau, IMillieu m) { 
		// TODO Auto-generated method
		return false;
	}

	public Plateau lirePlateau(IMillieu m) throws IOException { 

		int nbLignes, nbColonnes, cLigne, cColonne;
		String ligne, parts[];

		BufferedReader bf = new BufferedReader(
				new InputStreamReader (
						new FileInputStream (m.getAdresse())));

		ligne = bf.readLine();
		parts = ligne.split(" ");

		nbLignes = Integer.valueOf(parts[0]);
		nbColonnes	= Integer.valueOf(parts[1]);

		Plateau resultat = new Plateau(nbLignes, nbColonnes);

		cLigne = 0;
		cColonne = 0;

		while (bf.ready()) {

			ligne = bf.readLine();
			parts = ligne.split(" ");

			while (cColonne < nbColonnes) {

				char caractereLu = parts[cColonne].charAt(0);
				
				switch (caractereLu) {

				case '#': resultat.changeEtatA(new Coordonnee (cLigne, cColonne), 
						Case.PAS_EN_USE); break;

				case '0': resultat.changeEtatA(new Coordonnee (cLigne, cColonne), 
						Case.VIDE); break;

				case '1': resultat.changeEtatA(new Coordonnee (cLigne, cColonne), 
						Case.OCCUPEE); break;
				}

				cColonne++;
			}

			cLigne++;
			cColonne = 0;
		}

		bf.close();
		return resultat;
	}

	public boolean ecritListeCoup(List<Coup> coups, IMillieu m) throws IOException { 
	
		FileWriter writer = new FileWriter (m.getAdresse());
		
		for (Coup c : coups) {
			
			writer.write(c.toString() + "\n");
			
		}
		
		writer.close();
		return true;
	}

	public List<Coup> lireCoups(Fichier f) throws IOException {
		
		List<Coup> coups = new ArrayList<Coup> ();
		
		BufferedReader bf = new BufferedReader(
				new InputStreamReader (
						new FileInputStream (f.getAdresse())));

		String ligne, parts[];
		
		while (bf.ready()) {
			
			ligne = bf.readLine();
			parts = ligne.split(" ");
			
			int x  = Integer.valueOf(parts[0]),
				y = Integer.valueOf(parts[1]);
			
			Direction d;
			
			if (parts[2].compareTo("HAUT") == 0)
				d = new Direction(Direction.HAUT);
			else if (parts[2].compareTo("BAS") == 0)
				d = new Direction(Direction.BAS);
			else if (parts[2].compareTo("DROITE") == 0)
				d = new Direction(Direction.DROITE);
			else d = new Direction(Direction.GAUCHE);
			
			coups.add(new Coup(new Coordonnee(y,x), d));
			
		}
		
		return coups;
	} 

}

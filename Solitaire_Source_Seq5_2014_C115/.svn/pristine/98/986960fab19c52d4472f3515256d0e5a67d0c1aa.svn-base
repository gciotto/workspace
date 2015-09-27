package controleurs;

import entites.Fichier;
import entites.Plateau;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import entites.Coup;

public interface IControleurIO {


	public Plateau lirePlateau(IMillieu m) throws IOException;
	public boolean ecritPlateau(Plateau plateau, IMillieu m);
	public boolean ecritListeCoup(List<Coup> coups, IMillieu m) throws IOException;
	public List<Coup> lireCoups(Fichier f) throws IOException; 

}

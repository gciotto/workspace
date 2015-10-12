package controleurs;

import java.util.List;

import entites.Coup;
import entites.Fichier;

public interface IControleurInterface {

	public void metEnOperation();

	public void finirOperation(); 

	public void changeControleur (ControleurJeu nouveauControleur);

}

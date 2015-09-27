package entites;

public class Coup {

	private Direction direction;
	private Coordonnee coordonneeOrigine;


	public Coup(Coordonnee coord, Direction dir) { 
		
		this.coordonneeOrigine = new Coordonnee (coord.getLigne() ,coord.getColonne());
		this.direction = dir;
	 }
	
	public Direction getDirection() {
	 	 return direction; 
	}
	
	public Coordonnee getCoordonneeOrigine() {
	 	 return coordonneeOrigine; 
	}
	
	public Coordonnee getCoordonneeCible() {
		
		int l = 0, c = 0, 
				ligne = this.coordonneeOrigine.getLigne(), 
				colonne = this.coordonneeOrigine.getColonne();
		
		switch (this.direction.getDirectionCode()) {
		
		case Direction.HAUT : 	c = colonne;
								l = ligne - 2;
								break;
								
		case Direction.BAS : 	c = colonne;
								l = ligne + 2;
								break;
								
		case Direction.GAUCHE: 	c = colonne - 2;
								l = ligne;
								break;
								
		case Direction.DROITE: 	c = colonne + 2;
								l = ligne;
								break;
		
		}
		
		return new Coordonnee(l, c);
		
	}
	
	public Coordonnee getCoordonneeEliminee() {
		
		int l = 0, c = 0, 
				ligne = this.coordonneeOrigine.getLigne(), 
				colonne = this.coordonneeOrigine.getColonne();
		
		switch (this.direction.getDirectionCode()) {
		
		case Direction.HAUT : 	c = colonne;
								l = ligne - 1;
								break;
								
		case Direction.BAS : 	c = colonne;
								l = ligne + 1;
								break;
								
		case Direction.GAUCHE: 	c = colonne - 1;
								l = ligne;
								break;
								
		case Direction.DROITE: 	c = colonne + 1;
								l = ligne;
								break;
		
		}
		
		return new Coordonnee(l, c);
		
	}
	
	
	public String toString() { 
		
		return this.coordonneeOrigine.toString() + " " + this.direction.toString();
	 } 

}

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
	
	
	public String toString() { 
		
		return this.coordonneeOrigine.toString() + " " + this.direction.toString();
	 } 

}

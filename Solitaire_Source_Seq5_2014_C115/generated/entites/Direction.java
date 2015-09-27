package entites;

public class Direction {

	private int directionCode;
	
	public static final int DROITE 	= 0;
	public static final int GAUCHE 	= 1;
	public static final int BAS		= 2;
	public static final int HAUT	= 3;
	
	public Direction(int code) { 

		this.directionCode = code;		
	}
	
	public int getDirectionCode() {
		return directionCode; 
	}

	public String toString() { 
	
		String resultat = "";
		
		switch (this.directionCode) {
		
		case 0: resultat = "DROITE"; 	break;
		case 1: resultat = "GAUCHE"; 	break;
		case 2: resultat = "BAS";		break;
		case 3: resultat = "HAUT"; 	break;
		
		}
		
		return resultat;
	} 

}

import interaction.Input;


public class Prime {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int 	premier = Input.readInt("Saisissez le valeur du nombre"),
				i ;
		
		boolean estPremier = true;
		
		for (i = 2; i < premier && estPremier ; i++ ) {
			if ( premier % i == 0 ) 
				estPremier = false;
		}
		
		if (estPremier) 
			System.out.println(premier + " est premier.");
		else System.out.println(premier + " n'est pas premier. Il est divisé par "+  (i-1) +".");
	}

}

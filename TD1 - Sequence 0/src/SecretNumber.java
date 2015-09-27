import interaction.Input;

public class SecretNumber {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int 	graine = Input.secondsSinceMidnight(),
				n = ( 1277 * graine ) % 100,
				choix,
				nbCoups = 0;
		
		do {
			
			choix = Input.readInt("Saisissez votre choix ");
			
			nbCoups++;
			
			if (choix < n)
				System.out.println(choix+ " est trop petit");
			else if (choix > n)
				System.out.println(choix+ " est trop grand");
			
			else System.out.println("Voilà! " + choix);
			
		} while ( choix != n );

		System.out.println("Le nombre de coups utilisé a été "+ nbCoups);
		
	}

}

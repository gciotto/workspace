import interaction.Input;


public class PrimeFactors {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int nbPremier = Input.readInt("Saisissez le valeur du nombre");

		System.out.print(nbPremier + " = ");
		
		while (nbPremier != 1) {
			
			boolean dejaDivise = false;
			
			for (int i = 2; i <= nbPremier && !dejaDivise ; i++ ) {
				if ( nbPremier % i == 0 ) {
					dejaDivise = true;
										
					nbPremier /= i;
					
					if (nbPremier != 1 )
						System.out.print(i + " x ");
					else System.out.print(i);
				}
			}
			
		}
		
	}

}

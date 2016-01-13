import interaction.Input;

import java.util.Arrays;


public class Eratosthenes {

	static boolean[] initPrimes (int n) {
		
		boolean[] primes = new boolean [n + 1];

		primes[1] = false;
		
		for (int i = 2; i < primes.length; i++)
			primes[i] = true;
		
		return primes;
		
	}
	
	static void printPrimes (boolean[] primes) {
		
		for (int i = primes.length - 1; i > 1; i--) {
			
			for (int j = i - 1; j > 1 && primes[i] ; j--) {
				
				if ( i % j == 0 )
					primes[i] = false;
				
			}
			
		}
		
		for (int i = 1; i < primes.length; i++) 
			if ( primes [i] )
				System.out.print(i + " ");
		
	}
	
	static void testerFonction () {
		
		int size = Input.readInt("Saissisez le valeur n"); // Saisie de la taille du tableau
		boolean[] array = initPrimes(size); // Saisie du contenu du tableau
		System.out.println(Arrays.toString(array)); // Affichage
	    
	}
	

	static boolean[] eratosthenes (int n) {
		
		boolean[] primes = initPrimes(n);
		
		for (int i = primes.length - 1; i > 1; i--) {
			
			for (int j = i - 1; j > 1 && primes[i] ; j--) {
				
				if ( i % j == 0 )
					primes[i] = false;
				
			}
			
		}
		
		return primes;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int n = Input.readInt("Saissisez le valeur n"); // Saisie de la taille du tableau
		boolean[] primes = eratosthenes(n);
		
		for (int i = 1; i < primes.length; i++) 
			if ( primes [i] )
				System.out.print(i + " ");

	}

}

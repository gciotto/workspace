import interaction.Input;

public class Exponentiation {

	public static void main(String[] args) {
	
		int x, n, resultat;
	
		x = Input.readInt("Saisissez le valeur de x");
		
		while ( x != 0) {
		
			n = Input.readInt("Saisissez le valeur de n");
			resultat = 1;
			
			for (int i = 0; i < n ; i++)
				resultat *= x;
			
			System.out.println("La puissance " + x + " ^ " + n + " vaut " + resultat);
			
			x = Input.readInt("Saisissez le valeur de x");
		}
		
		System.out.println("Arret demandée.");
	}
}

/**
 * Des testes avec des grands nombres.
 * 
 * 2 ^ 30 = 1073741824
 * 2 ^ 31 = -2147483648
 * 2 ^ 32 = 0
 */

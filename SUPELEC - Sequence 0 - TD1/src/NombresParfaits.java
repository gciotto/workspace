import interaction.Input;


public class NombresParfaits {

	public static void main(String[] args) {
		
		int 	nombre = Input.readInt("Saisissez le valeur du nombre "),
				somme = 0; 
		
		for (int i = 1; i < nombre ; i++ ) {
			
			if ( nombre % i == 0 ) 
				somme += i;
			
		}

		if (somme == nombre)
			System.out.println(nombre + " est parfait.");
		
		else System.out.println(nombre + " n'est pas parfait. " +
						"La somme de ses diviseurs est "+ somme);
		
	}

}

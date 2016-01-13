import interaction.Input;


public class IntegerLength {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int nombre = Input.readInt("Saisissez valeur du nombre"),
			longueur = 0,
			temp = nombre;
		
		while (temp > 0) {
			
			longueur++;
			
			temp /= 10;
			
		}
		
		System.out.println("La longueur de "+ nombre + " est " + longueur );
		
	}

}

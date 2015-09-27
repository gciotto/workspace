import interaction.Input;


public class RandomNumbers {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int n = Input.readInt("Saisissez le valeur de la graine de la suite"),
				minimum = n;
		
		boolean premiereFois = true;
		
		for (int i = 0; i < 10; i++) {
		
			n = (1277*n) % 131072;
			
			if (premiereFois || n < minimum) {
				minimum = n;
				premiereFois = false;
			}
				
			System.out.print(n + " ");
			
		}

		System.out.println("\nLe minimum est "+ minimum);
	}

}

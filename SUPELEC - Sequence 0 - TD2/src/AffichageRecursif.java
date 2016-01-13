import interaction.Input;


public class AffichageRecursif {

	
	static void reverse () {
		
		int x = Input.readInt("Saisissez la valeur");
		
		if (x > 0) {
			reverse();
			System.out.print(x + " ");
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		reverse();
	}

}

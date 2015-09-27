import interaction.Input;


public class PGCD {

	static int pgcd (int a, int b) {
		
		if (a == b) return a;
		
		if (a < b) return pgcd (a, b - a); 
		
		return pgcd (a - b, b);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int a = Input.readInt("Saissisez le valeur a"),
			b = Input.readInt("Saissisez le valeur b");
		
		System.out.println("PGCD entre " + a + " et " +  b +" est " + pgcd(a, b));
	}

}

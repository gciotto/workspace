import interaction.Input;


public class Exponentiation {

	static int fastExponentiation (int x, int n) {
		
		if (n == 0) return 1;
		
		if ( (n % 2) == 0 ) 
			return fastExponentiation (x, n/2) * fastExponentiation (x, n/2);
		
		return x * fastExponentiation (x, (n-1)/2) * fastExponentiation (x, (n-1)/2);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int x = Input.readInt("Saissisez le valeur x"),
			n = Input.readInt("Saissisez le valeur n");
			
		System.out.println("Le valeur de " + x + "^" +  n +" est " + fastExponentiation(x, n));
	}

}

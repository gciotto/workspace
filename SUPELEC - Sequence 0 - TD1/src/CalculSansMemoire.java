public class CalculSansMemoire {

	public static void main(String[] args) {
		 
	       int a = 47 ;
	       int b = 18 ;
	       System.out.println("Avant : (" + a + ", " + b + ")");
	        
	       // Placer ici le code de l'échange des variables.
	       
	       b = a + b;
	       a = b - a;
	       b = b - a;
	 
	       System.out.println("Après : (" + a + ", " + b + ")");
	}
	
}

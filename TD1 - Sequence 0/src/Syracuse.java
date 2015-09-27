import interaction.Input;

public class Syracuse {

	public static void main(String[] args) {
		
		int n = Input.readInt("Saisissez le valeur de N"), tempsDeVol = 0;
		
		System.out.println("Les premiers nombres de la suite de Syracuse avant 1 sont ");
		
		 while (n != 1) {
			System.out.print(n + " ");
			
			if ( (n % 2) == 1 )
				n = 3*n + 1;
			else n /= 2;
			
			tempsDeVol++;
		}
		 
		System.out.println(n);
		
		System.out.println("Le temps de vol est "+ tempsDeVol);

	}

}

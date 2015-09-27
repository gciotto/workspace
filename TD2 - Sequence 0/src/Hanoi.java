import interaction.HanoiView;
import interaction.Input;


public class Hanoi {

	
	static void hanoi (int n, int in, int aux, int fin) {
		
		if (n > 0) {
			
			hanoi(n-1 , in , fin, aux);
			HanoiView.moveOneRing(in, fin);
			hanoi(n-1 , aux , in , fin );
			
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int nombre_anneaux = Input.readInt("Nombre d'anneaux");
			
		HanoiView.init(nombre_anneaux);
		
		hanoi(nombre_anneaux, 1 , 2 , 3);
	}

}

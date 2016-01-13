import interaction.Input;

import java.util.Arrays;


public class SelectionSort {

	static int[] createArray (int size) {
		
		int[] resultat = new int[size];
		
		for (int i = 0; i < size; i++) 
			resultat[i] = Input.readInt("Saissisez le "+ (i+1) + "eme valeur");
		
		return resultat;
		
	}
	
	static void testerFonction () {
		
		int size = 5; // Saisie de la taille du tableau
		int[] array = createArray(size); // Saisie du contenu du tableau
		System.out.println(Arrays.toString(array)); // Affichage
	    
	}
	
	static int indexOfMinimum (int [] array, int iStart) {
		
		int minimum = iStart;
		
		for (int i = iStart; i < array.length; i++) {
			if (array[i] < array[minimum])
				minimum = i;
		}
		
		return minimum;		
	}
	
	static void swap (int [] array, int iOrigine, int iDestination) {
		
		int temp = array[iOrigine];
		array[iOrigine] = array[iDestination];
		array[iDestination] = temp;		
		
	}
	
	static void sort (int [] array) {
	
		for (int i = 0; i < array.length; i++) {
			
			if ( i+1 < array.length ) {
				int min = indexOfMinimum(array, i+1);
				swap (array, min, i);
			}
		}
			
	}
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int size = Input.readInt("Saissisez la taille du tableau");
		
		int[] array = createArray(size);
		System.out.println(Arrays.toString(array));
		
		sort (array);
		System.out.println(Arrays.toString(array));;
	}

}

import interaction.Input;


public class Binaire {

	static void binary(int x){
		if (x !=0){
			
			binary(x/2);
			System.out.print(x%2);
			
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int x = Input.readInt("Saisissez la valeur");
		
		binary(x);

	}

}

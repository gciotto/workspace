
public class Fibonacci {

	
	public static void main(String[] args) {
		
		int fib1 = 1, fib2 = 1;
		
		System.out.println("Les 30 premiers nombres de la suite de Fibonacci sont ");
		
		for (int i = 0; i < 20 ; i++) {
			
			System.out.print(fib1 + " ");
			
			int temp = fib1;
			fib1 = fib2;
			fib2 += temp;
		}

	}

}

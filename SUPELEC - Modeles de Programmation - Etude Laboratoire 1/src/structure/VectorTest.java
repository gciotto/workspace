/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package structure;

public class VectorTest {

	public static void main(String[] args) {
		
		Vector 	m = new Vector ( 
					new double [] {0 , 1}),
				m2 = new Vector ( 
						new double [] {1 ,1});

		
		System.out.println(m.add(m2));
		System.out.println(m.subtract(m2));
	}
	
}

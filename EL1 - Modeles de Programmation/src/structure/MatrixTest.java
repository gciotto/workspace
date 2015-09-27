/****************************************
 * Gustavo CIOTTO PINTON
 * EL1 MP 
 ****************************************/
package structure;

public class MatrixTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Matrix 	m = new Matrix ( 
					new double [][] {
						{0 , 1} , {1 ,0}
					}),
				m2 = new Matrix ( 
						new double [][] {
								{1 , 1} , {1 ,1}
							});

		Vector 	v = new Vector ( 
				new double [] {0 , 1});
		
		System.out.println(m.add(m2));
		System.out.println(m.multiply(v));
		
		Matrix m3 = new Matrix(new double[][] {{1, -3, 7}, {6, -4, 2}});
		Vector v2 = new Vector(new double[]{1, 2, 3});
		System.out.println("m*v = " + m3.multiply(v2)) ;
	}

}

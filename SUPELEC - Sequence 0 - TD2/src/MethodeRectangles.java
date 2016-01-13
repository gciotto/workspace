
public class MethodeRectangles {

	
	static double f (double x) {
		return 5*x*x - 12; 
	}
	
	static void testerFonction () {
		
		double x = 0.5;
	    System.out.println(f(x));    // Doit afficher -10.75
	    
	    x = 1.8;
	    System.out.println(f(x));    // Doit afficher 4.199999999999999
	    
	    x = -8.45;
	    System.out.println(f(x));    // Doit afficher 345.0125
	    
	    x = 120.223;
	    System.out.println(f(x));    // Doit afficher 72255.848645
	    
	}
	
	static double integralF (double a, double b, int n) {
		
		double 	h = (b - a)/n,
				integ = 0;
		
		for (int i = 0; i < n; i++) {
			integ +=  f(a + i*h);
		}
		
		return integ *h;
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testerFonction();
		
		System.out.println(integralF(0.0, 2.0, 100));     // Doit afficher -10.865999999999998
		System.out.println(integralF(0.0, 2.0, 1000));    // Doit afficher -10.686659999999993
		
		/*
		 * Des valeurs affichées:
		 * 	-10.865999999999998
			-10.686659999999993
		 */
		
		/**
		 * à main:
		 * F(x) = 5/3 x^3 - 12x
		 * integral = 5/3*2^3 - 12*2 = -10,666666666666666666666666666667
		 */
	}

}

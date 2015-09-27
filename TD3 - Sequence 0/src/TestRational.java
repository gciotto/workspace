
public class TestRational {

	public static void main(String[] args) {
		
		Rational q = new Rational(3, 4);
		Rational r = new Rational(1, 6);

		System.out.println("q = " + q);
		System.out.println("r = " + r);

		System.out.println("gcd(12, 18) = " + Rational.gcd(12, 18));		
		
		System.out.println("q + r = " +q.add(r));
		System.out.println("q * r = " +q.multiply(r));
		
		Monomial m = new Monomial(q, 2);
		System.out.println("m = " + m);
		
		Monomial m2 = new Monomial(r, 4);
		System.out.println("m2 = " + m2);
		
		Monomial m3 = new Monomial(r.add(q), 0);
		System.out.println("m3 = " + m3);
		
		Monomial m4 = new Monomial(r.multiply(q), 1);
		System.out.println("m4 = " + m4);
		
		Monomial[] mon = new Monomial[] {m, m2, m3, m4};
		
		Polynome p = new Polynome(mon);
		System.out.println("p = " + p);
		System.out.println("degree = " + p.getDegree());
		
		Rational x = new Rational(2, 5);
		System.out.println("evaluate = " + m.evaluate(x));
		
		Monomial m5 = new Monomial(new Rational(7,1), 1);
		System.out.println("m5 = " + m5);
		
		Monomial m6 = new Monomial(new Rational(4,2), 0);
		System.out.println("m6 = " + m6);
		
		Monomial[] mon2 = new Monomial[] {m, m5, m6};
		
		Polynome p2 = new Polynome(mon2);
		System.out.println("p2 = " + p2);
		
		System.out.println("evaluate p2 = " + p2.evaluate(x));
		
		System.out.println("q approx " + q.approximate());
		System.out.println("r approx " + r.approximate());
		System.out.println("evaluate approx p2 = " + p2.approximate(x));
		
		System.out.println("canonize p2 = " + p2.canonize());

	}

}

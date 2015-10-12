import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Derivation {
    public static void main(String[] args) {
        Expression e = new Addition(
                new Product(new Number(3.0), new Variable('x')),
                new Number(2)
        );
        Expression dedx = e.derive('x');
        Expression dedy = e.derive('y');

        System.out.println("e = " + e.toString());
        System.out.println("de/dx = " + dedx.toString());

        System.out.println("      = " + dedx.simplify().toString());
        System.out.println("de/dy = " + dedy.toString());
        System.out.println("      = " + dedy.simplify().toString());
        System.out.println("");

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.println("Entrez une expression (vide pour finir) :");
                String exp = in.readLine();
                if ((exp == null) || (exp.length() == 0)) {
                    break;
                }
                Expression f = new ExpressionParser(exp).parse();
                Expression dfdx = f.derive('x');
                System.out.println("f = " + f.toString());
                System.out.println("  = " + f.simplify().toString());
                System.out.println("df/dx = " + dfdx.toString());
                System.out.println("      = " + dfdx.simplify().toString());
                System.out.println("");
            }
        } catch (java.io.IOException exc) {
            System.exit(1);
        }           
    }
}

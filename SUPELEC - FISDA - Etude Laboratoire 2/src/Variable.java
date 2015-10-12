/**
 * Représente une variable dont le nom est un unique caractère.
 */
public class Variable extends Expression {
	private char variable;
	
    public Variable(char nom) {
        this.variable = nom;
    }

    public Expression derive(char variable) {
        if (this.variable == variable)
        	return new Number(1);
        
    	return new Number(0);
    }
    
    public String toString() {
    	return Character.toString(this.variable);	 	
    }
}

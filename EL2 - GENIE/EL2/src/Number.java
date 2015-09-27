/**
 * Expression constante, dont la valeur est un <code>double</code>.
 */
public class Number extends Expression {
    
	private double number;
	
	public double getNumber() {
		return this.number;
	}
	
	/** Crée un Nombre de valeur <code>value</code>. */
	
    public Number(double value) {
    	number = value;
    }

    @Override
	public Expression derive(char variable) {
    	return new Number(0);
    }	
    

    public String toString() {
    	return Double.toString(this.number); 	 	
    }
    
    public boolean isConstant() {
        return true;
    }
    
    public boolean isNull() {
        return (this.number == 0);
    }
    
    public boolean isOne() {
    	return (this.number == 1);
    }
}

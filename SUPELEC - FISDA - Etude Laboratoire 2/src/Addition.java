/**
 * Représente un opérateur binaire "+"
 */
public class Addition extends BinaryOperator {
    
	public Addition(Expression arg1, Expression arg2) {
    	super(arg1, arg2);
    }

    public Expression derive(char variable) {
        // (u + v)' = u' + v'
    	
    	return new Addition(this.gauche.derive(variable), this.droite.derive(variable));
    }
    
    public String toString() {
    	return "( " + this.gauche + " + " + this.droite + " )"; 	 	
    }
    
    public Expression simplify() {  
    	
    	this.gauche = this.gauche.simplify();
    	this.droite = this.droite.simplify();
    	
    	if (this.gauche.isConstant() && this.droite.isConstant())
    		return new Number( ((Number)this.gauche).getNumber() + ((Number)this.droite).getNumber() );
    	
    	if (this.gauche.isNull())
        	return this.droite;
        
        if (this.droite.isNull())
        	return this.gauche;
        
        return this;
    }
}

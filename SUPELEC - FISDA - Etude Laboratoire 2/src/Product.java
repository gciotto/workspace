/**
 * Représente un opérateur binaire "*"
 */
public class Product extends BinaryOperator {
    public Product(Expression arg1, Expression arg2) {
        super(arg1, arg2);
    }

    public Expression derive(char variable) {
        // (uv)' = u'v + uv'
    	
    	return new Addition(
    			new Product(this.gauche.derive(variable), this.droite), 
    			new Product(this.gauche, this.droite.derive(variable)));
    }
    
    public String toString() {
    	return "( " + this.gauche + " * " + this.droite + " )"; 	 	
    }
    
    public Expression simplify() {  
    	
    	this.gauche = this.gauche.simplify();
    	this.droite = this.droite.simplify();
    	    	
    	if (this.gauche.isOne())
        	return this.droite;
    	
    	if (this.droite.isOne())
        	return this.gauche;
    	
    	if (this.droite.isOne() && this.gauche.isOne())
    		return new Number (1);
    	
    	if (this.droite.isNull() || this.gauche.isNull())
        	return new Number(0);
                
        return this;
    }
}

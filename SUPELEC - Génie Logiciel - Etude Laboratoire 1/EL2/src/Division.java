/**
 * Représente un opérateur binaire "/"
 */
public class Division extends BinaryOperator {
    public Division(Expression arg1, Expression arg2) {
    	 super(arg1, arg2);
    }

    public Expression derive(char variable) {
        // (u/v)' = (u'v - uv')/(vv)
        
    	Subtraction aux = new Subtraction(
    			new Product(this.gauche.derive(variable), this.droite), 
    			new Product(this.gauche, this.droite.derive(variable)));
    	
    	Product p = new Product(this.droite, this.droite);
    	
    	return new Division(aux, p);
    }
    
    public String toString() {
    	return "( " + this.gauche + " / " + this.droite + " )"; 	 	
    }
    
    public Expression simplify() {  
    	
    	this.gauche = this.gauche.simplify();
    	this.droite = this.droite.simplify();
    	
    	if (this.gauche.isNull())
        	return new Number(0);
        
    	if (this.droite.isOne())
    		return this.gauche;
    	
        return this;
    }
}

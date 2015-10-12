/**
 * classe de base des opérateurs binaires.
 */
public abstract class BinaryOperator extends Expression {

	protected Expression gauche, droite;
	
	/**
     * Crée un opérateur binaire ayant pour arguments <code>arg1</code>
     * et <code>arg2</code>.
     */
    protected BinaryOperator(Expression arg1, Expression arg2) {
        this.gauche = arg1;
        this.droite = arg2;
    }
    
}

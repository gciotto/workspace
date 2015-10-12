/**
 * Représente une expression arithmétique.
 */
public abstract class Expression {
    /**
     * Rend la dérivée de cette expression par rapport à la
     * variable de nom <code>variable</code>.
     */
    public abstract Expression derive(char variable);


    // Les méthodes suivantes ne sont nécessaires que pour la simplification
    // et ne seront pas codées dans un premier temps.

    
    /**
     * Rend une version si possible simplifiée de cette expression.
     */
    public Expression simplify() {
        return this; // par défaut, l'expression ne se simplifie pas
    }

    /**
     * Indique si cette expression est constante.
     */
    public boolean isConstant() {
        return false; // par défaut, une expression n'est pas constante
    }

    /**
     * Indique si cette expression est nulle (neutre pour l'addition).
     */
    public boolean isNull() {
        return false; // par défaut, une expression n'est pas nulle
    }

    /**
     * Indique si cette expression vaut 1 (est neutre pour la multiplication).
     */
    public boolean isOne() {
        return false; // par défaut, une expression ne vaut pas 1
    }

}

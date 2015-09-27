package el3;

import java.awt.Color;

/**
 * Cette classe représente des couleurs indexées à partir de 1.
 */
public class NodeColor {
    /**
     * Crée une couleur d'index 1.
     */
    public NodeColor() {
        this.index_ = 1;
    }

    /**
     * Crée une couleur d'index <code>index</code>.
     */
    private NodeColor(int index) {
        this.index_ = index;
    }

    /**
     * Rend une couleur d'index immédiatement supérieur à celui
     * de cette couleur.
     */
    public NodeColor getNextColor() {
        return new NodeColor(this.index_ + 1);
    }

    /**
     * Rend l'index de cette couleur.
     */
    public int index() {
        return this.index_;
    }

    /**
     * Indique si l'objet <code>o</code> est la même couleur
     * que cette couleur.
     * Si <code>o</code> n'est pas une couleur, rend <code>false</code>,
     * sinon, rend <code>true</code> uniquement si <code>o</code> a le même
     * index que cette couleur.
     */
    @Override
	public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        try {
            return this.index_ == ((NodeColor) o).index_;
        } catch (ClassCastException cce) {
            return false;
        }
    }

    /**
     * Rend le code de hachage de cette couleur.
     * Cette méthode est redéfinie afin d'assurer la cohérence
     * du code de hachage avec celui de <code>equals</code>. En effet,
     * deux objets égaux du point de vue de <code>equals</code> doivent
     * avoir le même code de hachage.
     */
    @Override
	public int hashCode() {
        return this.index_;
    }

    /**
     * Retourne une représentation textuelle de cette couleur.
     * Un certain nombre de couleurs ont un nom, pour les autres, la 
     * représentation est <code>couleur_</code> suivi de l'index de la couleur.
     */
    @Override
	public String toString() {
        if (this.index_ < noms_.length) {
            return noms_[this.index_];
        }
        return "couleur_" + this.index_;
    }

    /**
     * Retourne la couleur AWT (objet Color) associée à cette couleur.
     */
    public Color color() {
        if(this.index_ < colors.length) {
            return colors[this.index_];
        }
        double val = (((double)(this.index_ - colors.length))/11);
        val = val - Math.floor(val);
        return new Color(Color.HSBtoRGB((float)val, 1, 1)); 
    }

    /** Index de cette couleur. */
    private int index_;

    /** Table des noms de couleurs prédéfinis. */
    private static String[] noms_ = {
        "néant","rouge", "vert", "jaune", "bleu", "orange", "violet"
    };

    /** Table des couleurs AWT associées aux noms de couleurs prédéfinis */ 
    private static Color[] colors = {
        null, Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.ORANGE, Color.MAGENTA
    };
}

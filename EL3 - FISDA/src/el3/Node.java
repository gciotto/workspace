package el3;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Cette classe représente les noeuds d'un graphe non-orienté.
 * Chaque noeud porte une objet quelconque, et est relié à d'autres
 * noeuds du graphe par une relation d'adjacence symétrique.
 */
public class Node implements Comparable {
    private final Object contents;
    private HashSet<Node> adjacent_nodes;
    private final Point2D.Double position;

    /**
     * Crée un noeud portant l'objet <code>o</code> et situé à la
     * <code>position</code> donnée (pour l'outil de visualisation graphique).
     */
    protected Node(Object o, Point2D.Double position) {
        this.contents = o;
        this.adjacent_nodes = new HashSet<Node>();
        this.position = position;
    }

    /**
     * Rend le nombre de noeuds adjacents à ce noeud.
     */
    public int countAdjacentNodes() {
        return this.adjacent_nodes.size();
    }

    /**
     * Rend les noeuds adjacents à ce noeud sous une forme utilisable avec 
     * for each.
     */
    public Iterable<Node> adjacentNodes() {
        return this.adjacent_nodes;
    }

    /**
     * Connecte (rend adjacent) ce noeud au noeud <code>n</code>.
     * La relation d'adjacence étant symétrique, cela connecte aussi le
     * noeud <code>n</code> à ce noeud.
     */
    public void connectTo(Node n) {
        this.adjacent_nodes.add(n);
        n.adjacent_nodes.add(this);
    }

    /**
     * Déconnecte ce noeud (supprime l'adjacence si elle existait) du
     * noeud <code>n</code>. La relation d'adjacence étant symétrique,
     * cela déconnecte aussi le noeud <code>n</code> de ce noeud.
     */
    public void disconnectFrom(Node n) {
        this.adjacent_nodes.remove(n);
        n.adjacent_nodes.remove(this);
    }

    /**
     * Rend l'objet porté par ce noeud.
     */
    public Object getContents() {
        return this.contents;
    }

    /**
     * Rend une chaîne décrivant ce noeud.
     * Cette chaîne comporte la représentation du contenu du noeud, suivie
     * de la liste des noeuds adjacents entre <code>[</code> et <code>]</code>.
     */
    @Override
	public String toString() {
        StringBuffer rep = new StringBuffer();
        rep.append(this.contents.toString());
        rep.append(" [");
        boolean first = true;
        for (Node n: adjacentNodes()) {
            if (first) {
                first = false;
            } else {
                rep.append(", ");
            }
            rep.append(n.getContents().toString());
        }

        rep.append("]");
        return rep.toString();
    }

    /**
     * Fournit la position d'un noeud (utilisé par l'outil de visualisation
     * graphique)
     */
    public Point2D.Double getPosition() {
        return this.position;
    }

	@Override
	public int compareTo(Object arg0) {
		
		if (this.adjacent_nodes.size() < ((Node)arg0).adjacent_nodes.size())
			return -1;
		
		if (this.adjacent_nodes.size() > ((Node)arg0).adjacent_nodes.size())
			return 1;
		
		return 0;
	}
}

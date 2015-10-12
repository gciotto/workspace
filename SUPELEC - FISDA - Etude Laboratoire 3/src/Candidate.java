import java.util.HashMap;
import java.util.List;

import el3.*;

/**
 * Représente une situation dans l'algorithme à essais successifs.
 * Cette situation correspond à un coloriage du graphe en un certain 
 * nombre de couleurs inférieur à une limite fixe, et indique le prochain
 * noeud qu'il faudra essayer de colorer.
 */
public class Candidate {
	private Graph g;
	private int max_colors;
	private HashMap <Node, NodeColor> couleurs;
	private HashMap <Node, NodeColor[]> node_palette;   
	

	/**
	 * Crée une situation initiale (on n'a tenté de colorer aucun noeud) pour
	 * le coloriage du graphe <code>g</code>, le nombre maximum de couleurs 
	 * utilisables étant fixé au nombre de noeuds du graphe (cas le pire).
	 */
	public Candidate(Graph g) {
		
		this.g = g;
		
		this.couleurs = new HashMap<Node, NodeColor>();
		
		this.max_colors = g.size();
		
		this.node_palette = new HashMap<Node, NodeColor[]>();
		
		NodeColor[] palette = new NodeColor[this.max_colors];   
		
		if (this.max_colors > 0) {
			
			palette[0] = new NodeColor();
			for (int i = 1; i < palette.length; i++)
				palette[i] = palette[i - 1].getNextColor();
			
			for (Node n : g) {
				this.couleurs.put(n, null);
				this.node_palette.put(n, palette);
			}
		}

	}

	/**
	 * Crée une situation initiale (on n'a tenté de colorer aucun noeud) pour
	 * le coloriage du graphe <code>g</code>, le nombre maximum de couleurs 
	 * utilisables étant fixé à <code>max_colors</code>.
	 */
	public Candidate(Graph g, int max_colors) {
		
		this.g = g;
		
		this.couleurs = new HashMap<Node, NodeColor>();
		
		for (Node n : g)
			this.couleurs.put(n, null);
		
		this.max_colors = max_colors;
		
		this.node_palette = new HashMap<Node, NodeColor[]>();
		
		NodeColor[] palette = new NodeColor[this.max_colors];   
		
		if (this.max_colors > 0) {
			
			palette[0] = new NodeColor();
			for (int i = 1; i < palette.length; i++)
				palette[i] = palette[i - 1].getNextColor();
			
			for (Node n : g) {
				this.couleurs.put(n, null);
				this.node_palette.put(n, palette);
			}
		}
	
	}

	/**
	 * Rend le nombre maximal de couleurs autorisé pour ce coloriage.
	 */
	public int getMaxColors() {
		
		if (this.g!= null){
			return this.max_colors;
		}
		
		return 0;
	}

	/**
	 * Rend le nombre de couleurs effectivement utilisées pour ce coloriage.
	 */
	public int countUsedColors() {
		
		if (this.g!=null){
			int max = 0; 
			for (Node n : g) {
				
				if (this.couleurs.get(n) != null) {
					int couleur = this.couleurs.get(n).index();
					if (couleur > max)
						max = couleur;
				}
			}
			
			return max;
		}
		return 0;
	}

	/**
	 * Rend la couleur associée à un noeud dans ce coloriage.
	 * @return la couleur du noeud <code>n</code> s'il est coloré,
	 *         <code>null</code> sinon.
	 */
	public NodeColor getNodeColor(Node n) {
	
		return this.couleurs.get(n);
	}

	/**
	 * Indique si un noeud est coloré dans cette situation.
	 */
	public boolean isColored(Node n) {
		
		return ( this.couleurs.get(n) != null );  
	}

	/** Colore le noeud <code>n</code> avec la couleur <code>c</code>. */
	protected void setNodeColor(Node n, NodeColor c) {
		
		this.couleurs.put(n, c);
	}

	/** Supprime la couleur du noeud <code>n</code>. */
	protected void unsetNodeColor(Node n) {
		
		this.couleurs.put(n, null);	
	}

	/**
	 * Indique si cette situation est une solution.
	 */
	public boolean isSolution() {
		
		if (this.countUsedColors() > this.max_colors)
			return false;
		
			
		for (Node n : this.g) {
			
			NodeColor n_couleur = this.couleurs.get(n);
			
			if (!this.isColored(n))
				return false;
			
			for (Node m : n.adjacentNodes()) {
				NodeColor m_couleur = this.couleurs.get(m);
				
				if (!this.isColored(m))
					return false;
				
				if (n_couleur.equals(m_couleur))
					return false;
			}
		}
		
		return true;
	}

	/**
	 * Essaie de colorier le graphe avec au plus le nombre maximal de
	 * couleurs indiqué lors de la création de la situation.
	 * @return <code>true</code> si le graphe a pu être colorié, 
	 *         <code>false</code> sinon.
	 */
	public boolean color() {
		
		this.sortList();
		return color(this.g.begin());
	}

	/**
	 * Essaie de colorier le graphe en commençant par le noeud
	 * qui se trouve à la position indiquée par le curseur
	 * <code>current</code>, en utilisant au plus le nombre maximal 
	 * de couleurs indiqué lors de la création de la situation.
	 * @return <code>true</code> si le graphe a pu être colorié, 
	 *         <code>false</code> sinon.
	 */
	public boolean color(Cursor<Node> current) {
		
		if (this.g.size() == 0)
			return false;
		
		if (this.isSolution())
			return true;
		
		Node n = current.element();
		
		for (int i = 0; i < this.node_palette.get(n).length; i++) {
			
			if (this.isPossibleColoring(i,n)) {
				
				this.setNodeColor(n, this.node_palette.get(n)[i]);
				
				if (current == this.g.end()) {
					if (this.isSolution())
						return true;
				}
				
				else if (color(this.g.next(current)))
					return true;
			}
			
		}		
		
		this.unsetNodeColor(n);
		return false;
	}
	
	private boolean isPossibleColoring (int color, Node n) {
		
		for (Node m : n.adjacentNodes()) {
			
			NodeColor m_color = this.getNodeColor(m);
			if (m_color != null)
				if (m_color.equals(this.node_palette.get(n)[color]))
					return false;			
		}
		
		return true;
	}

	/** Rend le graphe associé à cette situation. */
	protected Graph getGraph() {
		return g;
	}

	/**
	 * Rend une représentation textuelle de la situation.
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Coloriage en " + countUsedColors() 
				+ " couleurs sur "
				+ getMaxColors()
				+ " autorisées."
				+ System.getProperty("line.separator"));
		for (Node n: getGraph()) {
			buf.append(n.toString());
			NodeColor c = getNodeColor(n);
			if (c != null) {
				buf.append(" (" + c.toString() + ")");
			}
			buf.append(System.getProperty("line.separator"));
		}
		return buf.toString();
	}
	
	public void sortList() {
		
		int i = 0;
			 
		while (i < this.g.getNode_list().size()) {
			
			int j = i + 1;
			while (j < this.g.getNode_list().size()) {
				if (this.g.getNode_list().get(j).compareTo(this.g.getNode_list().get(i)) > 0) {
					
					Node n = this.g.getNode_list().get(i);
					this.g.getNode_list().set(i, this.g.getNode_list().get(j));
					this.g.getNode_list().set(j, n);
					
				}			
				j++;
			}	
			i++;
		}
			
	}
}

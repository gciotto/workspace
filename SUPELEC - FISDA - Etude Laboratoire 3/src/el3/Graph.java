package el3;

import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.awt.geom.Point2D;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Cette classe représente des graphes non-orientés.
 * Un graphe non-orienté est un ensemble de noeuds muni d'une relation
 * d'adjacence symétrique (si <code>N1</code> est adjacent à <code>N2</code>,
 * <code>N2</code> est adjacent à <code>N1</code>).
 * Chaque noeud porte une valeur qui peut être n'importe quel objet.
 */
public class Graph implements Iterable<Node>, CursorList<Node> {
	/** liste des noeuds du graphe. */
	private ListWithCursors<Node> node_list;
	private boolean has_coordinates;
	
	/**
	 * Crée un graphe vide (sans noeud).
	 */
	public Graph() {
//		liste_noeuds_ = new ArrayListWithCursors<Noeud>();
		this.node_list = new LinkedListWithCursors<Node>();
	}

	/**
	 * Ajoute un noeud {@code n} au graphe.
	 */
	public Node addNode(Node n) {
		this.node_list.add(n);
		return n;
	}
	
	/**
	 * Supprime le noeud <code>n</code> de ce graphe.
	 */
	public void deleteNode(Node n) {
		// On supprime le noeud de la liste
		this.node_list.remove(n);
		// Puis on le supprime de toutes les listes d'adjacences.
		for (Node current: this) {
			Iterator<Node> iter = current.adjacentNodes().iterator();
			while (iter.hasNext()) {
				if (iter.next() == n) {
					iter.remove();
				}
			}
		}
	}
	
	/**
	 * Rend un itérateur sur les noeuds de ce graphe.
	 */
	@Override
	public ListIterator<Node> iterator() {
		return this.node_list.listIterator();
	}
		
	/**
	 * Rend le nombre de noeuds de ce graphe.
	 */
	public int size() {
		return this.node_list.size();
	}
	
	/**
	 * Lit la description d'un graphe dans le fichier de nom
	 * <code>nomFichier</code> et rend le graphe correspondant.
	 * Un graphe est décrit à raison d'un noeud par ligne. Chaque ligne
	 * comporte le nom du noeud, suivi de la liste des noms des noeuds
	 * qui lui sont adjacents.
	 * <p>
	 * Les lignes vides sont ignorées, de même que celles qui débutent
	 * par le caractère <code>#</code>.
	 * Les noms de noeuds sont séparés par les caractères : espace, virgule et
	 * crochets ( <code>[</code> et <code>]</code> ).
	 * <p>
	 * La relation d'ajacence étant symétrique, il n'est pas nécessaire de 
	 * donner les adjacences redondantes dans la description.
	 * <p>
	 * L'exemple suivant est une description valide d'un graphe à 3 noeuds, 
	 * dans laquelle on a omis le noeud 1 dans la liste d'adjacence de 2, ainsi
	 * que les noeuds 1 et 2 dans celle de 3.
	 * <br>
	 * <pre>
	 * # Exemple de graphe à 3 noeuds
	 * #       1
	 * #      / \
	 * #     2---3
	 * 
	 * 1 [2,3]
	 * 2 [3]
	 * 3
	 * </pre>
	 */
	public static Graph readFromFile(String nomFichier)
								 throws FileNotFoundException, IOException {
		HashMap<Node,List<String>> adjacent_nodes = new HashMap<Node,List<String>>();
		Graph g = new Graph();
		g.has_coordinates = true;
		FileReader stream = new FileReader(nomFichier);
		BufferedReader buf = new BufferedReader(stream);
		String line = null;
		while ((line = buf.readLine()) != null) {
			// on ignore les lignes vides
			if (line.compareTo("") == 0) {
				continue;
			}
			// ainsi que celles qui débutent par '#' (commentaires)
			if (line.charAt(0) == '#') {
				continue;
			}
			// On découpe la ligne suivant ' ', ',', '[', ']', '(' et ')'
			StringTokenizer toks = new StringTokenizer(line, " [,]()", true);
			// On lit le nom du noeud
			String nomNoeud = toks.nextToken();
			double node_x=0, node_y=0;
			Node current = null;
			// puis celui des noeuds adjacents
			List<String> adj = new ArrayListWithCursors<String>();

			int st = 0;
			// etat 0: lecture des voisins
			// etat 1: lecture de X
			// etat 2: lecture de Y
			while (toks.hasMoreTokens()) {
				String tok = toks.nextToken();

				if (tok.equals("(")) {
					st=1;
				} else if (st==1 && tok.equals(",")) {
					st=2;
				} else if (tok.equals(")")) {
					current = g.addNode(new Node(nomNoeud, new Point2D.Double(node_x, node_y)));
				} else { // on lit un caractère qui n'est pas un délimiteur
					switch(st) {
						case 0: 
							if(!(tok.equals(",") || tok.equals("[") || tok.equals("]") || tok.equals(" "))) {
								adj.add(tok); 
							}
							break;
						case 1:
							node_x = Double.parseDouble(tok);
							break;
						case 2:
							if (!tok.equals(" ")) {
								node_y = Double.parseDouble(tok);
							}
							break;
					}
				}
			}

			// cas où les coordonées ne sont pas indiquées
			if (current == null) {
				current = g.addNode(new Node(nomNoeud, null));
				g.has_coordinates = false;
			}

			// on place cette liste dans une table car les noeuds adjacents
			// ne sont pas forcément dans le graphe pour l'instant.
			adjacent_nodes.put(current, adj);
		}
		buf.close();
		// Une fois que tous les noeuds ont été placés dans le graphe,
		// on peut établir les connexions selon la table d'adjacence
		// remplie durant la lecture du fichier.
		for (Node courant: g) {
			for (String nom: adjacent_nodes.get(courant)) {
				Node trouve = null;
				for (Node test: g) {
					if (test.getContents().equals(nom)) {
						trouve = test;
						break;
					}
				}
				if (trouve == null) {
					// Noeud adjacent ne se trouvant pas dans le graphe !
					System.out.println("Erreur, pas de noeud \""
														 + nom
														 +"\" dans le graphe");
					System.exit(1);
				} else {
					courant.connectTo(trouve);
				}
			}
		}
		return g;
	}
	
	/**
	 * Rend une représentation textuelle de ce graphe.
	 * Cette représentation est compatible avec la syntaxe de
	 * description reconnue par <code>lire</code>.
	 */
	@Override
	public String toString() {
		StringBuilder rep = new StringBuilder();
		for (Node n: this) {
			rep.append(n.toString());
			rep.append(System.getProperty("line.separator"));
		}
		return rep.toString();
	}
	
	/**
	 * Indique si les noeuds du graphe sont tous munis de coordonnees.
	 * L'outil de visualisation graphique ne peut être utilisé que si c'est le
	 * cas.
	 */
	public boolean hasCoordinates() {
		return this.has_coordinates;
	}
	
	/** Rend un curseur sur le premier noeud du graphe. */
	@Override
	public Cursor<Node> begin() {
		return this.node_list.begin();
	}

	/** Rend un curseur sur le dernier noeud du graphe. */
	@Override
	public Cursor<Node> end() {
		return this.node_list.end();
	}

	/** Rend le noeud se trouvant sous le curseur <code>c</code>. */
	@Override
	public Node get(Cursor<Node> c) {
		return this.node_list.get(c);
	}

	/** Rend un curseur sur le noeud suivant celui indiqué par <code>c</code>. */
	@Override
	public Cursor<Node> next(Cursor<Node> c) {
		return this.node_list.next(c);
	}

	/** Rend un curseur sur le noeud précédant celui indiqué par <code>c</code>. */
	@Override
	public Cursor<Node> previous(Cursor<Node> c) {
		return this.node_list.previous(c);
	}

	public ListWithCursors<Node> getNode_list() {
		return node_list;
	}
	
}

import el3.*;

/**
 * Classe principale permettant de trouver un coloriage optimal
 * d'un graphe.
 */
public class Coloring {
    /**
     * Fonction principale, prend en argument le nom du fichier
     * contenant la description du graphe à colorier.
     */
    public static void main(String[] args) {
        Graph g = null;
        if (args.length < 1) {
            System.err.println("# Usage : java Coloriage <fichier>");
            System.exit(1);
        } else {
            try {
                g = Graph.readFromFile(args[0]);
            } catch (Exception e) {
                System.err.println(e.toString());
                System.exit(1);
            }
        }

        // Situation initiale (aucun noeud coloré) pour le graphe
        Candidate initial_candidate = new Candidate(g);

        // Pour mesurer le temps d'exécution de l'algorithme.
        Chrono clock = new Chrono();

        // Meilleure solution trouvée pour l'instant.
        Candidate best_coloring = null;

        // Indique si on a pu trouver une solution.
        boolean ok = true;

        while (ok) {
            System.out.println("Recherche d'une solution en au plus "
                    + initial_candidate.getMaxColors()
                    + " couleur(s).");
            clock.start();
            ok = initial_candidate.color();
            clock.stop();
            System.out.println("Durée : " + clock.toString());

            if (!ok) {
                System.out.println("Pas de solution.");
            } else {
                System.out.println("Une solution en "
                        + initial_candidate.countUsedColors()
                        + " couleur(s).");
                // On mémorise la meilleure situation trouvée
                best_coloring = initial_candidate;
                // Et on regarde si on ne peut pas faire mieux
                if (initial_candidate.countUsedColors() < 2) {
                    break;
                }
                initial_candidate = new Candidate(g, initial_candidate.countUsedColors() - 1);
            }
            System.out.println();
        }
        // Temps mis pour trouver la meilleure solution, et pour
        // savoir qu'il n'y en a pas de meilleure.
        System.out.println("Temps total : " + clock.total());
        if (best_coloring != null) {
            System.out.println("Coloriage optimal :");
            System.out.println(best_coloring.toString());
        }
    } 
}

package el3;
import java.util.List;

/**
 * Cette interface définit le protocole des listes
 * (telles que fournies dans java.util) qui supportent 
 * les curseurs.
 * 
 * @author Frédéric Boulanger
 *
 * @param <T> type des éléments de la liste.
 */
public interface ListWithCursors<T> extends List<T>, CursorList<T> {
	// Pas de méthode, ce n'est qu'un type racine
}

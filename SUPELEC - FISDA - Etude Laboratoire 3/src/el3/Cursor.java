package el3;
/**
 * Représente une position dans une collection. L'ordre entre 
 * les positions est déterminé par la collection, ou par un 
 * itérateur sur la collection.
 * 
 * @author Frédéric Boulanger
 *
 * @param <T> le type des éléments contenus dans la collection
 */
public interface Cursor<T> {
	public T element();
}

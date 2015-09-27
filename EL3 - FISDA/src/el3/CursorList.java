package el3;

/**
 * Cette interface définit le protocole supplémentaire
 * des listes qui supportent les curseurs.
 * 
 * @author Frédéric Boulanger
 *
 * @param <T> type des éléments de la liste.
 */
public interface CursorList<T> {
	/**
	 * Rend un curseur sur le début de la liste, 
	 * ou <code>null</code> si la liste est vide.
	 */
	public Cursor<T> begin();
	/**
	 * Rend un curseur sur la fin de la liste, 
	 * ou <code>null</code> si la liste est vide.
	 */
	public Cursor<T> end();
	/**
	 * Rend la position qui suit <code>c</code> dans 
	 * l'ordre imposé par la liste sur ses éléments,
	 * ou <code>null</code> si <code>c</code> est la
	 * position de la fin de la liste.
	 */
	public Cursor<T> next(Cursor<T> c);
	/**
	 * Rend la position qui précède <code>c</code> dans 
	 * l'ordre imposé par la liste sur ses éléments,
	 * ou <code>null</code> si <code>c</code> est la
	 * position du début de la liste.
	 */
	public Cursor<T> previous(Cursor<T> c);
	/**
	 * Rend l'élément qui se trouve à la position <code>c</code> 
	 * dans la liste.
	 */
	public T get(Cursor<T> c);
}

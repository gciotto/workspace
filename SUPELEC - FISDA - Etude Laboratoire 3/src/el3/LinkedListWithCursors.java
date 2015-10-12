package el3;

import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ListIterator;

/**
 * Une implémentation de LinkedList (de java.util) qui
 * supporte les curseurs.
 * 
 * @author Frédéric Boulanger
 *
 * @param <T> type des éléments de la liste
 */
public class LinkedListWithCursors<T> extends AbstractSequentialList<T> implements ListWithCursors<T> {
	/** Premier maillon de la liste */
	private Link first;
	/** Dernier maillon de la liste */
	private Link last;
	/** Taille de la liste */
	private int size;
	
	/**
	 * Maillon utilisé pour chaîner les éléments de la liste.
	 * 
	 * @author Frédéric Boulanger
	 *
	 */
	private class Link implements Cursor<T> {
		/** Lien vers le maillon suivant */
		public Link next;
		/** Lien vers le maillon précédent */
		public Link previous;
		/** Element porté par le maillon */
		public T element;
		/**
		 * Construit un maillon portant l'élément <code>obj</code>,
		 * inséré entre les maillons <code>prev</code> et <code>nxt</code>.
		 */
		public Link(T obj, Link prev, Link nxt) {
			this.next = nxt;
			this.previous = prev;
			this.element = obj;
		}
		@Override
		public T element() {
			return this.element;
		}
	}
	/**
	 * Iterateur sur les listes chaînées.
	 * 
	 * @author Frédéric Boulanger
	 *
	 */
	private class Iterator implements ListIterator<T> {
		/** Position courante de l'itérateur */
		private Link pos_;
		/** Dernière position utilisée pour <code>next()</code> 
		 *  ou <code>previous()</code>.
		 */
		private Link current_;
		/** Position avant le premier élément */
		private Link begin_;
		/** Position après le dernier élément */
		private Link end_;
		
		/** 
		 * Construit un itérateur sur la liste à partir de
		 * l'élément d'indice <code>i</code>.
		 */
		public Iterator(int i) {
			this.begin_ = new Link(null, null, LinkedListWithCursors.this.first);
			this.end_ = new Link(null, LinkedListWithCursors.this.last, null);
			this.pos_ = this.begin_;
			for (int j = 0; j < i; j++) {
				this.pos_ = this.pos_.next;
			}
			this.current_ = null;
		}
		/**
		 * Ajoute un élément après la position courante
		 * (donc avant l'élément rendu par <code>next()</code>.
		 */
		@Override
		public void add(T o) {
			Link n = new Link(o, this.pos_, this.pos_.next);
			if (this.pos_ == this.begin_) {
				LinkedListWithCursors.this.first = n;
				n.previous = null;
			}
			if (this.pos_.next != null) {
				this.pos_.next.previous = n;
			} else {
				LinkedListWithCursors.this.last = n;
			}
			this.pos_.next = n;
			this.current_ = null;
			LinkedListWithCursors.this.size++;
		}

		@Override
		public boolean hasNext() {
			return (this.pos_.next != null);
		}

		@Override
		public boolean hasPrevious() {
			return (this.pos_.previous != null);
		}

		@Override
		public T next() {
			this.current_ = this.pos_.next;
			if (this.current_ == null) {
				this.pos_ = this.end_;
				return null;
			}
			T obj = this.current_.element;
			this.pos_ = this.pos_.next;
			return obj;
		}

		@Override
		public int nextIndex() {
			if (this.pos_ == this.end_) {
				return LinkedListWithCursors.this.size;
			}
			Link l = this.begin_;
			int i = 0;
			while (l != this.pos_) {
				l = l.next;
				i++;
			}
			return i;
		}

		@Override
		public T previous() {
			this.current_ = this.pos_.previous;
			if (this.current_ == null) {
				this.pos_ = this.begin_;
				return null;
			}
			T obj = this.current_.element;
			this.pos_ = this.pos_.previous;
			return obj;
		}

		@Override
		public int previousIndex() {
			if (this.pos_ == this.begin_) {
				return -1;
			}
			Link l = this.end_;
			int i = LinkedListWithCursors.this.size - 1;
			while (l != this.pos_) {
				l = l.previous;
				i--;
			}
			return i;
		}

		@Override
		public void remove() {
			if (this.current_ == null) {
				throw new IllegalStateException();
			}
			if (this.current_.next == null) {
				LinkedListWithCursors.this.last = this.current_.previous;
			} else {
				this.current_.next.previous = this.current_.previous;
			}
			if (this.current_.previous == null) {
				LinkedListWithCursors.this.first = this.current_.next;
			} else {
				this.current_.previous.next = this.current_.next;
			}
			this.current_ = null;
			LinkedListWithCursors.this.size --;
		}

		@Override
		public void set(T o) {
			if (this.current_ == null) {
				throw new IllegalStateException();
			}
			this.current_.element = o;
		}
	}
	
	public LinkedListWithCursors() {
		this(null);
	}
	
	public LinkedListWithCursors(Collection <? extends T> c) {
		this.first = null;
		this.last = null;
		this.size = 0;
		if (c != null) {
			addAll(0, c);
		}
	}
	@Override
	public int size() {
		return this.size;
	}
	@Override
	public ListIterator<T> listIterator(int start) {
		return new Iterator(start);
	}
	@Override
	public Cursor<T> begin() {
		return this.first;
	}
	@Override
	public Cursor<T> end() {
		return this.last;
	}
	@Override
	public T get(Cursor<T> c) {
		return ((Link)c).element;
	}
	@Override
	public Cursor<T> next(Cursor<T> c) {
		return ((Link)c).next;
	}
	@Override
	public Cursor<T> previous(Cursor<T> c) {
		return ((Link)c).previous;
	}
}
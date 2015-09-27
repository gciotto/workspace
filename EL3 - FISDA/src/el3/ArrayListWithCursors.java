package el3;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Collection;

/**
 * Une implémentation d'ArrayList (de java.util) qui
 * supporte les curseurs.
 * 
 * @author Frédéric Boulanger
 *
 * @param <T> type des éléments de la liste
 */
public class ArrayListWithCursors<T> extends AbstractList<T> implements ListWithCursors<T> {
	private static final int defaultCapacity_ = 10;
	private static final int defaultCapacityIncrement_ = 10;
	
	private T[] storage_;
	private int size_;
	
	private class ArrayCursor implements Cursor<T> {
		public int idx;
		public ArrayCursor(int i) {
			this.idx = i;
		}
		@Override
		public T element() {
			return get(this);
		}
	}

	public ArrayListWithCursors() {
		this(defaultCapacity_);
	}
	public ArrayListWithCursors(int initialCapacity) {
		this.storage_ = makeArray(initialCapacity);
	}
	public ArrayListWithCursors(Collection<? extends T> c) {
		this.storage_ = makeArray(c.size());
		int i = 0;
		for (T elem : c) {
			this.storage_[i++] = elem;
		}
	}
	@SuppressWarnings("unchecked")
	private T[] makeArray(int size) {
		return (T[])Array.newInstance(Object.class, size);
	}
	@Override
	public T get(int i) {
		return this.storage_[i];
	}
	@Override
	public T set(int i, T elem) {
		T previousValue = this.storage_[i];
		this.storage_[i] = elem;
		return previousValue;
	}
	@Override
	public void add(int i, T elem) {
		if (i < 0 || i > this.size_) {
			throw new IndexOutOfBoundsException();
		}
		T[] newStorage = this.storage_;
		if ((this.size_ + 1) > this.storage_.length) {
			newStorage = makeArray(this.storage_.length + defaultCapacityIncrement_);
			for (int j = 0; j < i; j++) {
				newStorage[j] = this.storage_[j];
			}
		}
		for (int j = this.size_; j > i; j--) {
			newStorage[j] = this.storage_[j-1];
		}
		newStorage[i] = elem;
		this.storage_ = newStorage;
		this.size_ ++;
	}
	@Override
	public T remove(int i) {
		T previousValue = this.storage_[i];
		for (int j = i+1; j < this.size_; j++) {
			this.storage_[j-1] = this.storage_[j];
		}
		this.storage_[this.size_ - 1] = null;
		this.size_ --;
		return previousValue;
	}
	@Override
	public int size() {
		return this.size_;
	}
	public void shrink() {
		if (this.storage_.length == this.size_) {
			return;
		}
		T[] newStorage = makeArray(this.size_);
		for (int i = 0; i < this.size_; i++) {
			newStorage[i] = this.storage_[i];
		}
		this.storage_ = newStorage;
	}
	@Override
	public Cursor<T> begin() {
		if (this.size_ > 0) {
			return new ArrayCursor(0);
		} else {
			return null;
		}
	}
	@Override
	public Cursor<T> end() {
		if (this.size_ > 0) {
			return new ArrayCursor(this.size_ - 1);
		} else {
			return null;
		}
	}
	@Override
	public Cursor<T> next(Cursor<T> c) {
		ArrayCursor ac = (ArrayCursor)c;
		if ((ac.idx >= 0) && (ac.idx < (this.size_ - 1))) {
			return new ArrayCursor(ac.idx + 1);
		} else if (ac.idx == this.size_ - 1) {
			return null;
		} else {
			throw new IllegalArgumentException("Invalid cursor for this list");
		}
	}
	@Override
	public Cursor<T> previous(Cursor<T> c) {
		ArrayCursor ac = (ArrayCursor)c;
		if ((ac.idx > 0) && (ac.idx <= (this.size_ - 1))) {
			return new ArrayCursor(ac.idx - 1);
		} else if (ac.idx == 0) {
			return null;
		} else {
			throw new IllegalArgumentException("Invalid cursor for this list");
		}
	}
	@Override
	public T get(Cursor<T> c) {
		ArrayCursor ac = (ArrayCursor)c;
		if (ac.idx < this.size_) {
			return this.storage_[ac.idx];
		} else {
			throw new IllegalArgumentException("Invalid cursor for this list");
		}
	}
}


public class DeleteEvenNumbers implements Manipulation {

	private LinkedList list;

	public void start(LinkedList l) {
		this.list = l;
	}

	public void applyTo(Node p) {
		
		if (p.getValue() % 2 == 0)
			list.delete(p);
	}

}

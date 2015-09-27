
public class Eratosthenes implements Manipulation {

	private LinkedList list;
	
	public void start(LinkedList l) {
		list = l;
	}

	public void applyTo(Node p) {
		
		boolean arreter = false;
		
		for (Node n = this.list.getFirstNode(); n!= p && !arreter; n = n.getNextNode()) {
			if (p.getValue() % n.getValue() == 0) {
				this.list.delete(p);
				arreter = true;
			}
		}
		
	}

}

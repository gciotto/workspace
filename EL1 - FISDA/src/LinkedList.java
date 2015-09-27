
public class LinkedList {

	private Node first, last;

	public LinkedList(){
		this.first = null;
		this.last = null;
	}

	public Node getFirstNode() {
		return this.first;
	}

	public Node getLastNode() {
		return this.last;
	}

	public void delete(Node l) {

		Node n;
		for (n = this.first; n != null && n != l; n = n.getNextNode());

		if (n == null)
			throw new Error ("Le node l n'est pas dans la liste");

		if (n == this.first) {
			this.first = n.getNextNode();
		}

		if (n == this.last) {
			this.last = n.getPreviousNode();
		}

		if (n.getPreviousNode() != null )
			n.getPreviousNode().setNext(n.getNextNode());

		if (n.getNextNode() != null)
			n.getNextNode().setPrevious(n.getPreviousNode());

	}

	public Node insertBefore(Node l, int value) {

		Node nouveau = l.linkBefore(value);

		if (l == this.first)
			this.first= nouveau;

		return nouveau;
	}

	public Node insertAfter(Node l, int value) {

		Node nouveau = l.linkAfter(value);

		if (l == this.last)
			this.last = nouveau;

		return nouveau;

	}

	public void append(int value) {

		if (this.last != null)
			this.insertAfter(this.last, value); 
		else {
			this.last = new Node (value, null, null);
			this.first = this.last;
		}	
	}

	public void prepend(int value) {

		if (this.first != null)
			this.insertBefore(this.first, value);
		else {
			this.last = new Node (value, null, null);
			this.first = this.last;
		}
	}

	public String toString() {

		String resultat = "[";

		for (Node n = this.first; n != null; n = n.getNextNode()) {

			resultat = resultat + n.getValue();
			if (n.getNextNode() != null)
				resultat += ", ";
		}

		resultat += "]";

		return resultat;
	}

	public LinkedList map(Function f) {

		LinkedList l = new LinkedList();

		for (Node n = this.first; n != null; n = n.getNextNode()) {
			l.append(f.applyTo(n.getValue()));
		}

		return l;
	}
	
	public <T> T reduce(Operation<T> op) {
		for (Node n = this.first; n != null; n = n.getNextNode()) {
			op.applyTo(n.getValue());
		}

		return op.getResult();
	}
	
	public void manipulate(Manipulation m) {
		m.start(this);
		
		for (Node n = this.first; n != null; n = n.getNextNode()) {
			m.applyTo(n);
		}
	}
}

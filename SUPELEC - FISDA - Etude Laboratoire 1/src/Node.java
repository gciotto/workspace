
public class Node {
	
	private int value;
	private Node previous, next;

	
	public void setPrevious(Node previous) {
		this.previous = previous;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public Node(int value, Node previous, Node next) {
		this.value = value;
		this.previous = previous;
		this.next = next;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public Node getPreviousNode() {
		return this.previous;
	}
	
	public Node getNextNode() {
		return this.next;
	}
	
	public String toString() {
		
		return Integer.toString(this.value);
	}
	
	public Node linkAfter(int value) {
		
		Node nouveau = new Node (value, this, this.next);
		
		if (this.next != null) {
			this.next.previous = nouveau;
		}
		
		this.next = nouveau;
		
		return nouveau;
	}
	
	public Node linkBefore(int value) {
		
		Node nouveau = new Node (value, this.previous, this);
		
		if (this.previous != null) {
			this.previous.next = nouveau;
		}
		
		this.previous = nouveau;
		
		return nouveau;
		
	}
}

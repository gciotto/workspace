
public class NodeTest {

	public static void main(String[] args) {
		
	Node n4=new Node (2,null,null);
	System.out.println(n4.getValue());
	

	// Test Nodes
	System.out.println("Testing nodes and forward linking. Should produce 123");
	Node n2 = new Node(2,null,null);
	Node n3 = n2.linkAfter(3);
	Node n1 = n2.linkBefore(1);
	for (Node n = n1; n != null; n = n.getNextNode()) {
	  System.out.print(n.getValue());
	}
	System.out.println();
	System.out.println();
	System.out.println("Testing nodes and backward linking. Should produce 321");
	for (Node n = n3; n != null; n = n.getPreviousNode()) {
	  System.out.print(n.getValue());
	}
	System.out.println();
	System.out.println();
	}

}

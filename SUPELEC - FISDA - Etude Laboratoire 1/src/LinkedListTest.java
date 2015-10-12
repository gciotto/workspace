
public class LinkedListTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Test LinkedLists
		System.out.println("Testing append. Should produce \"1 2 3 \"");
		LinkedList l1 = new LinkedList();
		l1.append(1);
		l1.append(2);
		l1.append(3);
		for (Node n = l1.getFirstNode(); n != null; n = n.getNextNode()) {
		    System.out.print(n.getValue() + " ");
		}
		System.out.println();
		System.out.println();
		System.out.println("Testing prepend. Should produce \"3 2 1 \"");
		LinkedList l2 = new LinkedList();
		l2.prepend(1);
		l2.prepend(2);
		l2.prepend(3);
		for (Node n = l2.getFirstNode(); n != null; n = n.getNextNode()) {
		    System.out.print(n.getValue() + " ");
		}
		System.out.println();
		System.out.println();
		// Test toString()
		System.out.println("Testing toString. Should produce \"[1, 2, 3]\"");
		System.out.println(l1);
		System.out.println();

		LinkedList l3 = new LinkedList();
		for (int i = 0; i <= 20; i++) {
		    l3.append(i);
		}
		
		Square s = new Square();
		Mean m = new Mean();
		DeleteEvenNumbers del = new DeleteEvenNumbers();
		Eratosthenes er = new Eratosthenes();
		
		System.out.println("Moyenne = " +l3.reduce(m));
		System.out.println("Carre = " + l3.map(s));
		l3.manipulate(del);
		System.out.println("Delete even Numbers = " + l3);
		
		LinkedList l4 = new LinkedList();
		for (int i = 2; i <= 100; i++) {
		    l4.append(i);
		}
		
		l4.manipulate(er);
		System.out.println(l4);
	}

}


public class Sort {

	
	public static void selectionSort(LinkedList l ) {
		
		for (Node n = l.getFirstNode(); n != null; n = n.getNextNode()) {
			int min;
			Node aux = n;
			
			for (Node p = n.getNextNode(); p != null; p = p.getNextNode()) {
				if (p.getValue() < aux.getValue())
					aux = p;
			}
			
			min = aux.getValue();
			aux.setValue(n.getValue());
			n.setValue(min);			
		}
	}
	
	
	public static void main (String args[]) {
		

		System.out.println("# Selection sort of a random list");
		LinkedList to_be_sorted = new LinkedList();
		for (int i = 0; i < 20; i++) {
		    to_be_sorted.append((int)(Math.random() * 100));
		}
		System.out.println(to_be_sorted);
		Sort.selectionSort(to_be_sorted);
		System.out.println(to_be_sorted);
		System.out.println();
		
	}
}

import java.util.concurrent.Semaphore;


public class ThreadTest {

	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("Creation - Thread Principal");
		
		TSecondaire t1 = new TSecondaire(5000, "Thread 1 Secondaire");
		TSecondaire t2 = new TSecondaire(5000, "Thread 2 Secondaire");
	
		t1.start();	
		t2.start();
		
		t1.join();
		t2.join();
			
		System.out.println("Fin - Thread Principal");

	}

}

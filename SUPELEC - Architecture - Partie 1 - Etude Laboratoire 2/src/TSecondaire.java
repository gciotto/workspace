import java.util.concurrent.Semaphore;


public class TSecondaire extends Thread {

	private int duree;
	private String nom;

	public TSecondaire(int duree, String nom) {

		this.duree = duree;
		this.nom = nom;
	}

	public void run () {

		
		try {
			
			System.out.println(this.nom +" debut");
			Thread.sleep(this.duree);
			System.out.println(this.nom +" fin "+this.duree+ " s");
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}

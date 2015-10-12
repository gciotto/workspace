import java.util.concurrent.Semaphore;


public class BoiteALettres {

	private int taille, nbMessages, debutQueue, finQueue;
	private int[] messages;
	private Semaphore 	placeVide, messageDisponible,
						mutexFinQueue, mutexDebutQueue;
	
	public BoiteALettres (int t) {
		
		this.taille = t;
		this.nbMessages = 0;
		this.messages = new int[this.taille];
		this.debutQueue = 0;
		this.finQueue = 0;
		
		this.mutexDebutQueue = new Semaphore(1);
		this.mutexFinQueue = new Semaphore(1);
		this.placeVide = new Semaphore(taille);
		this.messageDisponible = new Semaphore(0);
		
	}
	
	public void deposerMessage (int m) throws InterruptedException {
		
		this.demanderPlacePourDeposerMessage();
	
		this.mutexFinQueue.acquire();
		
		this.messages[finQueue] = m;
		this.finQueue = (this.finQueue + 1) % this.taille;
		
		this.mutexFinQueue.release();
		
		this.permettreConsommateur();
		
	}
	
	private void demanderPlacePourDeposerMessage() throws InterruptedException {
		
		this.placeVide.acquire();
	}
	
	private void permettreConsommateur () {
		
		this.messageDisponible.release();
	}

	public int retirerMessage () throws InterruptedException {
		
		this.demanderConsommationDeMessage();
		
		this.mutexDebutQueue.acquire();
		
		int message = this.messages[this.debutQueue];
		this.debutQueue = (this.debutQueue + 1) % this.taille;
		
		this.mutexDebutQueue.release();
		
		this.permettreProducteur();
		
		return message;
	}

	private void permettreProducteur() {
		
		this.placeVide.release();
	}

	private void demanderConsommationDeMessage() throws InterruptedException {
		
		this.messageDisponible.acquire();
	}
	
}

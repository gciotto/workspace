import java.util.concurrent.Semaphore;


public class SiegesDisponibles {

	private int taille, nbMessages, debutQueue, finQueue, nbCoiffeurs;
	private Client[] messages;
	private Coiffeur[] coiffeur;
	private Semaphore 	placeVide, messageDisponible,
						mutexFinQueue, mutexDebutQueue,
						mutexCoiffeur;
	
	public SiegesDisponibles (int t, int nbCoiffeurs) {
		
		this.taille = t;
		this.nbMessages = 0;
		this.messages = new Client[this.taille];
		this.debutQueue = 0;
		this.finQueue = 0;
		this.coiffeur = new Coiffeur[nbCoiffeurs];
		this.nbCoiffeurs = 0;
		
		this.mutexDebutQueue = new Semaphore(1);
		this.mutexFinQueue = new Semaphore(1);
		this.placeVide = new Semaphore(taille);
		this.messageDisponible = new Semaphore(0);
		this.mutexCoiffeur = new Semaphore(1);
		
	}
	
	public void ajoutCoiffeur(Coiffeur c) {
		this.coiffeur[this.nbCoiffeurs++] = c;
	}
	
	public boolean arriverClient (Client m) throws InterruptedException {
		
		boolean aTrouveSiege = this.demanderPlacePourDeposerMessage();
	
		if (!aTrouveSiege) return false;
		
		this.mutexCoiffeur.acquire();
		
		boolean aTrouveCoiffeur = false;
		for (int i = 0; i < this.nbCoiffeurs && !aTrouveCoiffeur; i++)
			if (this.coiffeur[i].getStateCoiffeur() == Coiffeur.COIFFEUR_ENDORMI) {
				aTrouveCoiffeur = true;
				this.coiffeur[i].reveillerCoiffeur();
			}
		
		this.mutexCoiffeur.release();
		
		this.mutexFinQueue.acquire();
		
		this.messages[finQueue] = m;
		this.finQueue = (this.finQueue + 1) % this.taille;
		
		this.mutexFinQueue.release();
		
		this.permettreConsommateur();
		
		return true;
	}
	
	private boolean demanderPlacePourDeposerMessage() throws InterruptedException {
		
		return this.placeVide.tryAcquire();
	}
	
	private void permettreConsommateur () {
		
		this.messageDisponible.release();
	}

	public Client couperClient () throws InterruptedException {
		
		boolean reussi = this.demanderConsommationDeMessage();
		
		if (!reussi) return null;
		
		this.mutexDebutQueue.acquire();
		
		Client message = this.messages[this.debutQueue];
		this.debutQueue = (this.debutQueue + 1) % this.taille;
		
		this.mutexDebutQueue.release();
		
		this.permettreProducteur();
		
		return message;
	}

	private void permettreProducteur() {
		
		this.placeVide.release();
	}

	private boolean demanderConsommationDeMessage() throws InterruptedException {
		
		return this.messageDisponible.tryAcquire();
	}
	
}

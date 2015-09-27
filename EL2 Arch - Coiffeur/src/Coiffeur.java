import java.util.concurrent.Semaphore;


public class Coiffeur extends Personne {

	private String nom;
	private SiegesDisponibles messages;
	private int nbMessages;
	private int stateCoiffeur;

	public static int COIFFEUR_DISPONIBLE = 0,
					  COIFFEUR_ENDORMI = 1;
	
	public Coiffeur (String nom, SiegesDisponibles m) {

		this.nom = nom;
		this.messages = m;
		this.stateCoiffeur = 0;
		this.fauteil = new Semaphore(0);
	}

	

	public void run () {

		System.out.println(this.nom + " debut.");

		while (true) {

			try {

				long duree = (long) (Math.random() * 1000);
				Thread.sleep(duree);

				Client m = this.messages.couperClient();

				if (m == null) {

					System.out.println(this.nom + ": il n'y a pas de clients. Il s'endort.");
					this.stateCoiffeur = Coiffeur.COIFFEUR_ENDORMI;
					this.fauteil.acquire();

				}
				else {

					System.out.println(this.nom + ": c'est le tour du "+ m);

					System.out.println(this.nom + " commence "+ m);

					m.commencerCouper();

					duree = (long) (Math.random() * 1000);
					Thread.sleep(duree);

					System.out.println(this.nom + " a fini "+ m);

					m.finiCouper();
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	public int getStateCoiffeur() {
		
		return this.stateCoiffeur;
	}
	
	public void reveillerCoiffeur() {

		System.out.println(this.nom + " se reveille.");
		this.stateCoiffeur = Coiffeur.COIFFEUR_DISPONIBLE;
		this.fauteil.release();

	}

}


public class Consommateur extends Thread {

	private String nom;
	private int nbMessages; 
	private BoiteALettres messages;

	public Consommateur (String nom, int nbMessages, BoiteALettres m) {

		this.nom = nom;
		this.nbMessages = nbMessages;
		this.messages = m;
	}


	public void run () {

		System.out.println(this.nom + " debut.");

		for (int i = 0; i < this.nbMessages; i++) {

			try {

				long duree = (long) (Math.random() * 1000);

				Thread.sleep(duree);
				
				int m = this.messages.retirerMessage();

				System.out.println(this.nom + " retirer "+ i + "o message. Message: "+ m);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		System.out.println(this.nom + " fin.");

	}

}

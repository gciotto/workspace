
public class Producteur extends Thread {

	private String nom;
	private int nbMessages; 
	private BoiteALettres messages;

	public Producteur (String nom, int nbMessages, BoiteALettres m) {

		this.nom = nom;
		this.nbMessages = nbMessages;
		this.messages = m;

	}


	public void run () {

		System.out.println(this.nom + " debut.");

		for (int i = 0; i < this.nbMessages; i++) {

			try {

				long duree = (long) (Math.random() * 10000);

				Thread.sleep(duree);
				
				this.messages.deposerMessage(i);

				System.out.println(this.nom + " deposer "+ i + "o message.");

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		System.out.println(this.nom + " fin.");

	}

}

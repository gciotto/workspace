
public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		int nbClients = 100,
			nbCoiffeurs = 2;

		SiegesDisponibles messages = new SiegesDisponibles(4, nbCoiffeurs);

		Coiffeur[] coiffeurs = new Coiffeur[nbCoiffeurs]; 

		for (int i = 0; i < nbCoiffeurs; i++) {

			Coiffeur c =  new Coiffeur("Coiffeur "+ (i+1), messages);
			coiffeurs[i] = c;
			messages.ajoutCoiffeur(c);
			c.start();
		}


		for (int i = 0; i < nbClients; i++){

			Client c = new Client(i, messages);
			c.start();
			
			long duree = (long) (Math.random() * 10000);

			Thread.sleep(duree);
		}

		for (int i = 0; i < nbCoiffeurs; i++) 
			coiffeurs[i].join();

		System.out.println("Fin.");
	}

}

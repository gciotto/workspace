
public class Main {

	public static void main(String[] args) throws InterruptedException {

		int nbProducteurs = 1,
				nbConsommateurs = 1;

		BoiteALettres messages = new BoiteALettres(4);

		Consommateur[] consommateurs = new Consommateur[nbConsommateurs]; 
		Producteur[] producteurs = new Producteur[nbProducteurs];

		for (int i = 0; i < nbConsommateurs; i++) {

			Consommateur c =  new Consommateur("Consommateur "+ (i+1), 4, messages);
			consommateurs[i] = c;
			c.start();
		}


		for (int i = 0; i < nbProducteurs; i++){

			Producteur p =  new Producteur("Producteur "+ (i+1), 8, messages);
			producteurs[i] = p;
			p.start();
		}


		for (int i = 0; i < nbConsommateurs; i++) 
			consommateurs[i].join();

		for (int i = 0; i < nbProducteurs; i++)
			producteurs[i].join();
		
		System.out.println("Fin.");
	}

}

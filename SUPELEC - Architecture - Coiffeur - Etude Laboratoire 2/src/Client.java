import java.util.concurrent.Semaphore;

public class Client extends Personne {

	private int id;
	private SiegesDisponibles sieges;
	private Semaphore siege;

	public Client (int id, SiegesDisponibles sieges) {
		this.id = id;		
		this.sieges = sieges;
		this.siege = new Semaphore(0);
		this.fauteil = new Semaphore(0);
	}

	public void run() {

		try {

			System.out.println("Client " + this.id + " essaie de s'asseoir");

			if (!this.sieges.arriverClient(this))
				System.out.println("Client " + this.id + " n'atend pas le coiffeur.");

			else {
			
				this.siege.acquire();

				System.out.println("Client " + this.id + " est choisi.");

				this.fauteil.acquire();

				System.out.println("Client " + this.id + " est fini. Il sort.");
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void commencerCouper() {
		this.siege.release();
	}

	public void finiCouper() {
		this.fauteil.release();
	}

	public String toString() {

		return "client "+ this.id;
	}

}

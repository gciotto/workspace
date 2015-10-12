
public class Mean extends Operation<Double> {

	double sum;
	int longueur;
	
	public Mean() {
		this.sum = 0;
		this.longueur = 0;
	}
	
	public void applyTo(int value) {
		this.longueur++;
		this.sum += value;
	}

	public Double getResult() {
		
		return new Double(this.sum / this.longueur);
	}

}

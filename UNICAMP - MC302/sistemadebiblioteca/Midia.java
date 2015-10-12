package sistemadebiblioteca;

public class Midia extends Digital {
	private int duracao;
	public Midia(String titulo, int id) {
		super(id, titulo);
	}
	public Midia(String titulo, int id, int duracao) {
		super(id, titulo);
		setDuracao(duracao);
	}
	public int getDuracao() {
		return duracao;
	}
	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}	
}
package sistemadebiblioteca;

public class Livro extends Fisico {
	private int edicao;
	private String autor;
	
	public Livro(String titulo, int id, int edicao, String autor) {
		super(id, titulo);
		this.setEdicao(edicao);
		this.setAutor(autor);
	}
	
	public int getEdicao() {
		return edicao;
	}
	public void setEdicao(int edicao) {
		this.edicao = edicao;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	
}
package sistemadebiblioteca;

public abstract class AbstractFactory {
	public Titulo newTitulo(int id, String titulo) {
		return  new Titulo(id,titulo);
	}
		
	public abstract Titulo newLivro(String titulo, int id, int edicao, String autor);
	public abstract Titulo newMidia(String titulo, int id, int duracao);
}

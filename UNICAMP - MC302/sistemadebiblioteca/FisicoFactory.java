package sistemadebiblioteca;

public class FisicoFactory extends AbstractFactory {
	private static FisicoFactory instance = null;
	
	public static FisicoFactory getInstance(){
		if (FisicoFactory.instance == null)
			FisicoFactory.instance  = new FisicoFactory();
		return FisicoFactory.instance ;
	}
	
	private FisicoFactory(){}
	
	public Titulo newLivro(String titulo, int id, int edicao, String autor) {
		return new Livro(titulo, id, edicao, autor);
	}

	public Titulo newMidia(String titulo, int id, int duracao) {
		return null;
	}

}

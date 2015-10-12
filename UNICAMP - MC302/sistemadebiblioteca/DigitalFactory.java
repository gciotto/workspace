package sistemadebiblioteca;

public class DigitalFactory extends AbstractFactory {

	private static DigitalFactory instance = null;
	
	public static DigitalFactory getInstance(){
		if (DigitalFactory.instance == null)
			DigitalFactory.instance  = new DigitalFactory();
		return DigitalFactory.instance ;
	}

	private DigitalFactory(){}
	
	public Titulo newLivro(String titulo, int id, int edicao, String autor) {
		return null;
	}

	public Titulo newMidia(String titulo, int id, int duracao) {
		return  new Midia(titulo, id,  duracao);
	}

}

package sistemadebiblioteca;

import java.util.ArrayList;

public class Titulo {
	
	private int id;
	private String titulo;
	private ArrayList<Exemplar> exemplares;
	
	public ArrayList<Exemplar> getExemplares() {
		return exemplares;
	}

	public void setExemplares(ArrayList<Exemplar> exemplares) {
		this.exemplares = exemplares;
	}

	public Titulo(int id, String titulo) {
		setId(id);
		setTitulo(titulo);
		exemplares = new ArrayList<Exemplar>();
	}
	
	public void adicionarExemplar(Exemplar e){
		exemplares.add(e);
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	// retorna um exemplar disponivel
	public Exemplar retornaExemplarDisponivel() throws BibliotecaException{
		for(Exemplar e : this.getExemplares())
			if (e.getSituacao() == EstadoExemplar.DISPONIVEL)
				return e;
			// caso nao haja exemplar disponivel, lança-se uma excecao
		throw new BibliotecaException(BibliotecaException.EXEMPLAR_INDISPONIVEL);
	}
	
	// retorna um exemplar que foi bloqueado pelo professor com idProfessor
	public Exemplar temExemplarBloqueadoPor(int idProfessor) {
		for(Exemplar e : this.getExemplares())
			if (e.getSituacao() == EstadoExemplar.BLOQUEADO 
					&& e.getIdProfessor() == idProfessor)
				return e;
		return null;
	}
	
}

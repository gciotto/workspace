package sistemadebiblioteca;

public class Professor extends Usuario {
	public Professor() {
		super();
		this.setMaximoLivro(4);
		this.setQuantidadePermitida( 15 );
	}
	public Professor(String nome, int id){
		super();
		setNome(nome);
		setMaximoLivro(4);
		setId(id);
		//professor pode emprestar o livro por 15 dias
		setQuantidadePermitida( 15 );
	}
	public boolean isProfessor(){
		return true;
	}
	public boolean isUsuario(){
		return false;
	}
	
	// bloquear livro por um professor  
	public boolean bloquear(Titulo e) throws BibliotecaException {
		Exemplar ex = e.retornaExemplarDisponivel();
		int idProfessor = getId();
		return ex.bloquear(idProfessor);
	}
	
	public boolean desbloquear(Titulo e) throws BibliotecaException {
		int idProfessor = getId();
		Exemplar bloqueado = e.temExemplarBloqueadoPor(idProfessor);
		// nao achou nenhum exemplar bloqueado por este professor  
		if (bloqueado == null)	throw new BibliotecaException(BibliotecaException.ERRO_BLOQUEADO);
		return bloqueado.desbloquear(idProfessor);		
	}
}
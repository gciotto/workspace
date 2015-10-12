package sistemadebiblioteca;

public class Aluno extends Usuario {
	public Aluno() {
		super();
		this.setQuantidadePermitida( 7 );
		this.setMaximoLivro(3);
	}
	public Aluno(String nome, int id){
		super();
		this.setNome(nome);
		this.setId(id);
		this.setMaximoLivro(3);
		//aluno pode emprestar o livro por 7 dias
		this.setQuantidadePermitida( 7 );
	}
	public boolean isAluno(){
		return true;
	}
	public boolean isUsuario(){
		return false;
	}
}
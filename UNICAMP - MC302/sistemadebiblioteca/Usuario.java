package sistemadebiblioteca;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
	private String nome;
	private int id;
	private List<Exemplar> exemplarEmprestado;
	private int quantidadePermitida, maximoLivro;
	private boolean bloqueado;
	
	public Usuario(){
		this.exemplarEmprestado = new ArrayList<Exemplar>();
		//usuario normal pode emprestar o livro por 3 dias
		this.setQuantidadePermitida( 3 );
		this.setMaximoLivro(2);
		bloqueado = false;
	}
	public Usuario(String nome, int id){
		this.setNome(nome);
		this.setId( id );
		this.exemplarEmprestado = new ArrayList<Exemplar>();
		//usuario normal pode emprestar o livro por 3 dias
		this.setQuantidadePermitida( 3 );
		this.setMaximoLivro(2);
		bloqueado = false;
	}
		
	
	public boolean isBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	// retorna o indice do titulo e na lista 
	public int estaComLivroEmprestado(Titulo e) throws BibliotecaException{
		if((this.getExemplarEmprestado() != null) &&
				(!this.getExemplarEmprestado().isEmpty())) {
			int i = 0;
			for (Exemplar a : getExemplarEmprestado()){
				if (a.getT().getId() == e.getId())
					return i;
				i++;
			}
		}
		// lanca uma excecao caso o titulo nao seja encontrado
		throw new BibliotecaException(BibliotecaException.TITULO_INEXISTENTE);
	}
	
	// realiza a operacao de emprestar um exmplear
	public boolean emprestaExemplar(Exemplar exemplar) throws BibliotecaException {
		if (exemplar == null) throw new BibliotecaException(BibliotecaException.TITULO_INEXISTENTE);
		// verifica se esta apto
		if(isAptoAEmprestar())
			if (exemplar.empresta()){
				// adiciona livro na lista de livros emprestados
				exemplarEmprestado.add(exemplar);
				return true;
			}
		
		return false;
	}
	
	// devolve exemplar 
	public boolean devolveExemplar(int indice) throws BibliotecaException {
		if (indice < 0) throw new BibliotecaException(BibliotecaException.TITULO_INEXISTENTE);
		
		Exemplar e = getExemplarEmprestado().get(indice);
		if( e != null){
			// remove da lista de exemplares emprestados.
			this.exemplarEmprestado.remove(indice);
			return e.devolver();
		}
		
		return false;
	}
	
	public String getNome(){
		return this.nome;
	}
	public void setNome(String name){
		this.nome = name;
	}
	public int getId(){
		return this.id;
	}
	public void setId(int x){
		this.id = x;
	}
	public boolean isAtrasado(int diasEmprestado){
		if( diasEmprestado > this.getQuantidadePermitida() )
			return true;
		else
			return false;
	}
	public boolean isAptoAEmprestar(){
		if(this.exemplarEmprestado.size() < this.getMaximoLivro()) return true;
		return false;
	}
	public boolean isProfessor(){
		return false;
	}
	public boolean isAluno(){
		return false;
	}
	public boolean isUsuario(){
		return true;
	}
	public List<Exemplar> getExemplarEmprestado() {
		return exemplarEmprestado;
	}
	public void setExemplarEmprestado(List<Exemplar> exemplarEmprestado) {
		this.exemplarEmprestado = exemplarEmprestado;
	}
	public int getQuantidadePermitida() {
		return quantidadePermitida;
	}
	public void setQuantidadePermitida(int quantidadePermitida) {
		this.quantidadePermitida = quantidadePermitida;
	}
	public int getMaximoLivro() {
		return maximoLivro;
	}
	public void setMaximoLivro(int maximoLivro) {
		this.maximoLivro = maximoLivro;
	}
}
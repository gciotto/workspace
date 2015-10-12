package sistemadebiblioteca;

import java.util.List;

public class ControleBiblioteca {
	private CadastroUsuario cadUsuarios;
	private CadastroTitulo cadTitulo;
	private static int emprestados = 0;
	private static int bloqueados = 0;
	
	private static ControleBiblioteca instance = null;
	
	public static ControleBiblioteca getInstance(){
		if (ControleBiblioteca.instance == null)
			ControleBiblioteca.instance  = new ControleBiblioteca();
		return ControleBiblioteca.instance ;
	}
	
	public static int getBloqueados() {
		return bloqueados;
	}
	
	private ControleBiblioteca(){
		this.cadTitulo = new CadastroTitulo();
		this.cadUsuarios = new CadastroUsuario();
	}
	public void calcularPrazoDeDevolucao(Usuario user){
		if( ( user != null ) && ( !user.getExemplarEmprestado().isEmpty() ) ){
			Exemplar ex = user.getExemplarEmprestado().get(0);
			if(ex != null) System.out.println(ex.toString());
		}
	}
	
	public static int getEmprestados() {
		return emprestados;
	}
	// cadastra usuarios
	public void cadastrarUsuario(String nome, int idNovo, int categoria) throws BibliotecaException{
		Usuario user = buscarUsuario(idNovo);
		// verifica se ja nao existe o usuario
		if (user != null) throw new BibliotecaException(BibliotecaException.USUARIO_JA_EXISTENTE);
		cadUsuarios.cadastrar(nome, idNovo, categoria);
	}
	
	//cadastra titulos
	public void cadastrarTitulo(Titulo t, int quant) throws BibliotecaException {
		Titulo title = buscaTitulo(t.getId());

		//verifica se ja nao existe o titulo com mesmo id
		if (title != null) throw new BibliotecaException(BibliotecaException.TITULO_JA_EXISTENTE);
	
		cadTitulo.cadastrar(t, quant);
	}
	
	
	public void devolverExemplar(int idTitulo, int idUser, int diasEmprestado) throws BibliotecaException{
		//verifica se existe o titulo
		Titulo ex = this.buscaTitulo(idTitulo);
		if(ex == null)	throw new BibliotecaException(BibliotecaException.TITULO_INEXISTENTE);
		
		//verifica se existe o usuario
		Usuario user = buscarUsuario(idUser);
		if (user == null)	throw new BibliotecaException(BibliotecaException.USUARIO_INEXISTENTE);
		
		int indice = user.estaComLivroEmprestado(ex);
		
		// atualiza o estado de exemplar e usuario
		boolean res = user.devolveExemplar(indice);
		if( ( res ) && ( user.isAtrasado(diasEmprestado) ) ){
			int tempoPermitido = user.getQuantidadePermitida();
			int qtdDiasAtrasado = diasEmprestado - tempoPermitido;
			System.out.println(user.getNome()+ " esta "+qtdDiasAtrasado+" dias atrasado.");
			user.setBloqueado(true);
			bloqueados++;
		}
		else if(res) { System.out.println("Devolucao do exemplar "+ex.getTitulo()+" id = "+ex.getId()); }
		emprestados--;
	}
	
	// lista exemplares emprestados
	public void listaExemplarEmprestado(int idUsuario) throws BibliotecaException{
		Usuario user = buscarUsuario( idUsuario );
		if( (user.getExemplarEmprestado().size() > 0 ) ){
			// imprime na tela
			for (Exemplar ex : user.getExemplarEmprestado() )
				System.out.println(ex.toString());
		}
		else System.out.println("O usuario nao tem exemplar");
	}
	
	public boolean emprestarExemplar(int idTitulo, int idUsuario) throws BibliotecaException {
		//verifica se o exemplar existe
		
		Exemplar exemplarAptoAEmprestar ;
		Titulo ex = this.buscaTitulo( idTitulo);
		if (ex == null)	throw new BibliotecaException(BibliotecaException.TITULO_INEXISTENTE);
		exemplarAptoAEmprestar = ex.retornaExemplarDisponivel();

		//se ele existe e realmente nao foi emprestado, atualiza os dados do usuario
		Usuario user = buscarUsuario(idUsuario);
		if (user.isBloqueado()) throw new BibliotecaException(BibliotecaException.USUARIO_BLOQUEADO);
		
		// tenta emprestar e imprime o resultado. Nao eh possivel quando o usuario jah emprestou quantidade maxima. 
		boolean resultado = user.emprestaExemplar(exemplarAptoAEmprestar);
		if(resultado) {
			System.out.println("Emprestimo do exemplar "+ex.getTitulo()+" id "+ex.getId()+"/"+exemplarAptoAEmprestar.getId()+" pelo(a) "+user.getNome());
			emprestados++;
		}
		else System.out.println(user.getNome()+" ja emprestou quantidade maxima de exemplares.");
		return resultado;

	}
	
	public CadastroUsuario getCadUsuarios() {
		return cadUsuarios;
	}
	public void setCadUsuarios(CadastroUsuario cadUsuarios) {
		this.cadUsuarios = cadUsuarios;
	}
	public CadastroTitulo getcadTitulo() {
		return cadTitulo;
	}
	public void setcadTitulo(CadastroTitulo cadTitulo) {
		this.cadTitulo = cadTitulo;
	}
	public int getQuantidadeDeUsuarios(){
		return this.getCadUsuarios().getListaUsuarios().size();
	}
	// busca usuario por id. Faz a checagem na lista de usuarios.
	public Usuario buscarUsuario(int id) throws BibliotecaException{
		List<Usuario> listaUsuario = this.getCadUsuarios().getListaUsuarios();
		if( listaUsuario != null )
			for(int i = 0; i < listaUsuario.size(); i++){
				Usuario user = listaUsuario.get(i);
				if( id == user.getId()) return user;
			}
		
		return null;
	}
	
	// busca semelhante ao busca usuario.
	public Titulo buscaTitulo(int id) throws BibliotecaException{
		List<Titulo> listaTitulo = this.getcadTitulo().getListaTitulos();
		if( listaTitulo != null )
			for(int i = 0; i < listaTitulo.size(); i++){
				Titulo ex = listaTitulo.get(i);
				if( id == ex.getId()) return ex;
			}
		
		return null;
		//throw new BibliotecaException ("Titulo não encontrado. Certique-se que digitou o id corretamente.");
	}
	public int getQuantidadeDeTitulos(){
		return this.getcadTitulo().getListaTitulos().size();
	}
	
	// busca usuario atraves do nome.
	public Usuario buscaUsuarioPorNome(String nome) {
		List<Usuario> listaUsuario = this.getCadUsuarios().getListaUsuarios();
		if( listaUsuario != null )
			for(int i = 0; i < listaUsuario.size(); i++){
				Usuario user = listaUsuario.get(i);
				if( nome.compareTo(user.getNome()) == 0) return user;
			}
		
		return null;
	}


}
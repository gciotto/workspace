package sistemadebiblioteca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
public class Terminal {
	private ControleBiblioteca controlBib;
	public Terminal(){
		this.setControlBib(ControleBiblioteca.getInstance());
	}
	/**
	 * @param args
	 */
	public void iniciarSistema() {
		int idUsuario = 0;
		int op;
		System.out.println("\nAtendimento aos usuarios");
		do {
			try {
				idUsuario = this.getIDUsuario();
				// seleciona usuario
				while( idUsuario > -1) {
					op = this.getOperacao(); // seleciona operacao
					this.executaOperacao(idUsuario,op); // e executa
					idUsuario = this.getIDUsuario();
				}
			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage());
				e.printStackTrace();
			}
			// trata excecoes de biblioteca
			catch (BibliotecaException e) {
				System.err.println(e.getMessage());
			}
		}
		while( idUsuario > -1); 
	}
	/**
	 * Esse metodo pega qual foi a operacao selecionada pelo usuario
	 * e retorna o codigo dessa operacao
	 * @param usuario
	 * @return
	 * @throws IOException
	 * @throws BibliotecaException 
	 */
	private int getOperacao() throws IOException, BibliotecaException {
		int op=getInt("operacao:\n1=empresta, 2=devolve, 3=ver exemplar emprestado, 4=bloquear exemplar, 5= desbloquear exemplar, 6= cadastrar usuario, 7= cadastrar titulo, 8= estatisticas, 9=sair",1,9);
		return(op);
	}
	/**
	 * Esse metodo retorna o id, que foi digitado pelo usuario do sistema.
	 * @return
	 * @throws IOException
	 * @throws BibliotecaException 
	 */
	private int getIDUsuario() throws IOException, BibliotecaException {
		ControleBiblioteca controlBib = this.getControlBib();
		int qtdUsuarios = controlBib.getQuantidadeDeUsuarios();
		int id = getInt("id do usuario ( ou digite -1 para terminar)",-1, (qtdUsuarios -1));
		return id;
	}
	/**
	 * retorna um objeto Exemplar a partir do id selecionado pelo usuario
	 * @return
	 * @throws IOException
	 * @throws BibliotecaException 
	 */
	private int getIDExemplar() throws IOException, BibliotecaException {
		ControleBiblioteca controlBib = this.getControlBib();
		int qtdTitulos = controlBib.getQuantidadeDeTitulos();
		int id = this.getInt( "o id do Titulo" , -1 , (qtdTitulos) );
		return id;
	}
	
	private String getString() throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader (System.in));
		return r.readLine();	
	}
	
	private int getInt() throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader (System.in));
		return Integer.parseInt(r.readLine());	
	}
	
	private void executaOperacao(int idUsuario, int op) throws IOException, BibliotecaException {
		
		ControleBiblioteca controlBib = this.getControlBib();			
		if(op==3) controlBib.listaExemplarEmprestado(idUsuario);
		else{
			if(op==9){
				System.out.println("Obrigado por usar o sistema de bibliotecas.");
				System.exit(0);
			}
			if (op > 5 && op < 9) {
				// checa se é um professor. Somente um professor pode cadastrar.
				Usuario userCadastro = controlBib.buscarUsuario(idUsuario);
				//caso nao seja, lança-se uma exceção
				if (!userCadastro.isProfessor()) throw new BibliotecaException(BibliotecaException.ERRO_PRIVILEGIO);
				
				switch (op) {
					case 6: 
							// pergunta informacoes necessarias
							System.out.println("Digite o nome do usuario a ser cadastrado:");
							String nome = getString();
							
							// verfica se jah nao ha um usuario com essse nome
							if (controlBib.buscaUsuarioPorNome(nome) != null) throw new BibliotecaException(BibliotecaException.USUARIO_JA_EXISTENTE);								
							
							System.out.println("Digite a categoria (1=aluno/2=professor/3=usuario comum):");
							int categoria = getInt(); 
							
							// cadastra
							controlBib.cadastrarUsuario(nome, controlBib.getQuantidadeDeUsuarios(), categoria);
							System.out.println("Cadastrado com sucesso: "+nome+" (id = "+ (controlBib.getQuantidadeDeUsuarios()-1)+").");
							break;
					
					case 7: 
						// pergunta informacoes necessarias
							System.out.println("Digite o titulo dos exemplares a serem cadastrados:");
							String titulo = getString();
							
							System.out.println("Digite o tipo (1=livro/2=midia/3=outro):");
							int tipo = getInt();
							
							System.out.println("Digite a quantidade de exemplares diponiveis:");
							int quant = getInt(); 
							
							// verifica quantidade de exemplares
							if (quant < 1) throw new BibliotecaException(BibliotecaException.ERRO_QUANTIDADE);
						
							
							Titulo t = null;
							// pede informacao especifica de cada tipo
							if (tipo == 1) {
								System.out.println("Digite a edicao do livro:");
								int edicao = getInt();
								System.out.println("Digite o autor do livro:");
								String autor = getString();
								t = FisicoFactory.getInstance().newLivro(titulo, controlBib.getQuantidadeDeTitulos(), edicao, autor); 
							} else if (tipo == 2){
								System.out.println("Digite a duracao em minutos:");
								int duracao = getInt();
								t =  DigitalFactory.getInstance().newMidia(titulo, controlBib.getQuantidadeDeTitulos(),  duracao); 
							}
							else if (tipo == 3) t =  FisicoFactory.getInstance().newTitulo(controlBib.getQuantidadeDeTitulos(),titulo);
							
							// cadastra novo titulo
							controlBib.cadastrarTitulo(t, quant);
							System.out.println("Cadastrado com sucesso "+titulo+" (id = "+ (controlBib.getQuantidadeDeTitulos()-1)+").");
							break;
					case 8: System.out.println("Estatisticas:\nExemplares: "+CadastroTitulo.getQuant_exemplares()+" (livros= " + CadastroTitulo.getQuant_exemplares()+
							"; midias= "+CadastroTitulo.getQuant_midias()+"; emprestados= "+ControleBiblioteca.getEmprestados()+")\nUsuarios: "+CadastroUsuario.getQuant_usuarios()+" (alunos= "+
							CadastroUsuario.getQuant_alunos()+"; professores: "+CadastroUsuario.getQuant_professores()+"; suspensos= "+ControleBiblioteca.getBloqueados()+")");
					break;
				}
			}
			else {
				boolean r = false;
				int idTitulo = this.getIDExemplar();
				switch(op) {
				case 1: // emprestimo
					r = controlBib.emprestarExemplar(idTitulo, idUsuario);
					if (r) System.out.println("Exemplar emprestado com sucesso!");
					else   System.out.println("Nao foi possivel emprestar o exemplar.");
					break;
				case 2: // devolucao
					int diasEmprestado = this.getTempoDeLocacao();
					if( diasEmprestado < 0){
						System.err.println("Valor invalido");
						break;
					}
					controlBib.devolverExemplar(idTitulo, idUsuario, diasEmprestado);
					System.out.println("Exemplar devolvido com sucesso.");
					break;
				case 4: // bloqueio
					Usuario u = controlBib.buscarUsuario(idUsuario);
					// deve ser professor para bloquear ou desbloquear
					if (u.isProfessor()) {
						// recupera titulo
						Titulo e = controlBib.buscaTitulo(idTitulo);
						if (((Professor)u).bloquear(e))	System.out.println("Exemplar bloqueado com sucesso");
						else System.out.println("Erro! Verifique se o exemplar esta alugado ou ja bloqueado!");
					}
					else System.out.println("Somente professor podem executar esta operacao");
					break;
				case 5: // desbloqueio
					Usuario u1 = controlBib.buscarUsuario(idUsuario);
					// deve ser professor para bloquear ou desbloquear
					if (u1.isProfessor()) {
						// recupera titulo
						Titulo e = controlBib.buscaTitulo(idTitulo);
						if (((Professor)u1).desbloquear(e)) System.out.println("Exemplar desbloqueado com sucesso");
					}
					else throw new BibliotecaException(BibliotecaException.ERRO_PRIVILEGIO);
					break;
				default: throw new BibliotecaException(BibliotecaException.ERRO_OPCAO);
				}
			}
		}		
		
	}
	
	private int getInt(String str, int de, int ate) throws IOException, BibliotecaException {
		BufferedReader r = new BufferedReader(new InputStreamReader (System.in));
		StreamTokenizer st = new StreamTokenizer(r);
			System.out.println("Entre com "+str);
			try {st.nextToken();}
			catch (IOException e) {
				System.err.println("Erro na leitura do teclado");
				return(0);
			}
			if ((int)st.nval > ate || (int)st.nval < de) throw new BibliotecaException(BibliotecaException.ERRO_OPCAO_INVALIDA);
		
		return((int)st.nval);
	}
	protected ControleBiblioteca getControlBib() {
		return controlBib;
	}
	protected void setControlBib(ControleBiblioteca controlBib) {
		this.controlBib = controlBib;
	}
	protected int getTempoDeLocacao() throws IOException{
		BufferedReader r = new BufferedReader(new InputStreamReader (System.in));
		StreamTokenizer st = new StreamTokenizer(r);
		System.out.println("Por quantos dias o exemplar foi emprestado?");
		while(st.ttype != StreamTokenizer.TT_NUMBER ){
			st.nextToken();
		}
		return ((int)st.nval);
	}
}
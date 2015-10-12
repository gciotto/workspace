package sistemadebiblioteca;

import java.util.ArrayList;
import java.util.List;

public class CadastroUsuario {

	private List<Usuario> listaUsuarios;
	
	private static int quant_usuarios = 0;
	private static int quant_professores = 0;
	private static int quant_alunos = 0;
	
	public CadastroUsuario(){
		
		Usuario user1 = new Usuario("Maria",0);
		Usuario user2 = new Usuario("Mariana",1);
		quant_usuarios = 6;
		Aluno aluno1 = new Aluno("Jose",2);
		Aluno aluno2 = new Aluno("Joao",3);
		quant_alunos = 2;
		Professor prof1 = new Professor("Ana",4);
		Professor prof2 = new Professor("Andre",5);
		quant_professores = 2;
		
		this.listaUsuarios = new ArrayList<Usuario>();
		
		this.listaUsuarios.add(user1);
		this.listaUsuarios.add(user2);
		this.listaUsuarios.add(aluno1);
		this.listaUsuarios.add(aluno2);
		this.listaUsuarios.add(prof1);
		this.listaUsuarios.add(prof2);
		
	}
	
	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	
	// cadastra novo usuario
	public void cadastrar(String nome, int idNovo, int categoria) throws BibliotecaException {
		Usuario newUser = null;
		// instancia objeto corretamente
		switch (categoria) {
			case 1:  newUser = new Aluno(nome, idNovo); quant_alunos++; break;
			case 2:  newUser = new Professor(nome, idNovo); quant_professores++; break;
			case 3:  newUser = new Usuario(nome, idNovo); break;
			default: throw new BibliotecaException(BibliotecaException.CATEGORIA_INVALIDA);
		}
		quant_usuarios++;
		listaUsuarios.add(newUser);
	}

	public static int getQuant_usuarios() {
		return quant_usuarios;
	}

	public static int getQuant_professores() {
		return quant_professores;
	}

	public static int getQuant_alunos() {
		return quant_alunos;
	}
	
}

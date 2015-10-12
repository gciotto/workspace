package sistemadebiblioteca;

public class BibliotecaException extends Exception {

	// constantes de erros
	public static final int CATEGORIA_INVALIDA = 0, TITULO_JA_EXISTENTE = 1, USUARIO_JA_EXISTENTE = 2, TITULO_INEXISTENTE = 3,
							USUARIO_INEXISTENTE = 4, ERRO_BLOQUEADO = 5, ERRO_PRIVILEGIO = 6, ERRO_OPCAO = 7, EXEMPLAR_INDISPONIVEL = 8,
							ERRO_OPCAO_INVALIDA = 9, ERRO_QUANTIDADE = 10, OUTRA = 11, USUARIO_BLOQUEADO = 12;
	
	// mensagens disponiveis
	private static String[] mensagem = {"Categoria inválida. Digite novamente.","Titulo com id especificado ja existe.",
										"Usuario com este id ja existe.", "Exemplar nao encontrado. Certifique-se que digitou o id corretamente.",
										"Usuario nao encontrado. Certifique-se que digitou o id corretamente.", "Nao ha exemplar deste titulo bloqueado pelo professor.",
										"Somente professores podem executar esta operacao.", "Opcao invalida. Digite novamente.", "O exemplar esta indisponivel.",
										"Valor digitado é invalido. Certifique-se que o id ou opcao estão corretos.", "Quantidade de exemplares invalido.",
										"Erro.", "Usuario esta suspenso para emprestimos. Espere ate sua situacao se normalizar."};
	
	public BibliotecaException (int err) {
		super(mensagem[err]);
	}
}

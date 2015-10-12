package sistemadebiblioteca;

public abstract class EstadoExemplar {
	
	public static int BLOQUEADO 	= 0;
	public static int DISPONIVEL 	= 1;
	public static int EMPRESTADO 	= 2;
	
	public abstract int getSituacao();
	public abstract boolean emprestar(Exemplar e);
	public abstract boolean bloquear(Exemplar e);
	public abstract boolean desbloquear(Exemplar e);
	public abstract boolean devolver(Exemplar e);
}


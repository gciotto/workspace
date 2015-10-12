package sistemadebiblioteca;

public class Emprestado extends EstadoExemplar {

	public int getSituacao() {
		return EstadoExemplar.EMPRESTADO;
	}

	public boolean emprestar(Exemplar e) {
		return false;
	}

	public boolean bloquear(Exemplar e) {
		return false;
	}

	public boolean desbloquear(Exemplar e) {
		return false;
	}

	public boolean devolver(Exemplar e) {
		e.mudarEstado(DISPONIVEL);
		return true;
	}

}

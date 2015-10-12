package sistemadebiblioteca;

public class Disponivel extends EstadoExemplar {

	public int getSituacao() {
		return EstadoExemplar.DISPONIVEL;
	}

	public boolean emprestar(Exemplar e) {
		e.mudarEstado(EMPRESTADO);
		return true;
	}

	public boolean bloquear(Exemplar e) {
		e.mudarEstado(BLOQUEADO);
		return true;
	}

	public boolean desbloquear(Exemplar e) {
		return false;
	}

	public boolean devolver(Exemplar e) {
		return false;
	}

}

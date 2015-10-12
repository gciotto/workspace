package sistemadebiblioteca;

public class Bloqueado extends EstadoExemplar {

	public int getSituacao() {
		return EstadoExemplar.BLOQUEADO;
	}

	public boolean emprestar(Exemplar e) {
		return false;
	}

	public boolean bloquear(Exemplar e) {
		return false;
	}

	public boolean desbloquear(Exemplar e) {
		e.mudarEstado(DISPONIVEL);
		return true;
	}

	public boolean devolver(Exemplar e) {
		return false;
	}

}

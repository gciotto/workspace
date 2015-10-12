package sistemadebiblioteca;

import java.util.Calendar;
import java.util.GregorianCalendar;
public class Exemplar{
	
	private int id, idProfessor;
	private Titulo t;
	private GregorianCalendar dtEmprestimo;
	private EstadoExemplar state; // estado: padrao de projeto State
	
	public Exemplar(int id, Titulo t){
		this.setId(id);
		setT(t);
		this.mudarEstado(EstadoExemplar.DISPONIVEL);
	}
	
	public EstadoExemplar getState() {
		return state;
	}
	
	// funcao de mudança de estado
	public void mudarEstado(int newState) {
		if (newState == EstadoExemplar.BLOQUEADO) state = new Bloqueado();
		if (newState == EstadoExemplar.EMPRESTADO) state = new Emprestado();
		if (newState == EstadoExemplar.DISPONIVEL) state = new Disponivel();
	}
	
	public int getSituacao() {
		return state.getSituacao();
	}
	
	public boolean bloquear(int idProfessor) {
		if (state.bloquear(this)) {
			this.setIdProfessor(idProfessor);
			return true;
		}
		
		return false;
	}
	
	public boolean desbloquear(int idProfessor) {
		return state.desbloquear(this);
	}
	
	public boolean empresta() {
		if (state.emprestar(this)) {
			this.setDtEmprestimo(new GregorianCalendar());
			return true;
		}
		
		return false;
	}
	
	public boolean devolver(){
		if (state.devolver(this)) {
			this.setDtEmprestimo(null);
			return true;
		}
		return false;
	}
	
	public String getTitulo() {
		return t.getTitulo();
	}
	
	public GregorianCalendar getDtEmprestimo() {
		return dtEmprestimo;
	}
	
	public void setDtEmprestimo(GregorianCalendar dtEmprestimo) {
		this.dtEmprestimo = dtEmprestimo;
	}
	
	
	// imprime Exemplar
	public String toString() {
		String st = new String();
		if (getSituacao() == EstadoExemplar.DISPONIVEL)	return( t.getTitulo()+ " idExemplar "+ this.getId() + " disponivel");
		if (getSituacao() == EstadoExemplar.EMPRESTADO)	st = " emprestado em " + this.printDate( this.getDtEmprestimo() );
		if (getSituacao() == EstadoExemplar.BLOQUEADO)	st = " bloqueado por " + this.getIdProfessor();
		return( t.getTitulo() + st );
	}
	
	// imprime data de emprestimo
	protected String printDate(GregorianCalendar gc){
		String str = new String();
		str = String.valueOf( gc.get(Calendar.DAY_OF_MONTH) );
		str = str + "/" + String.valueOf( gc.get(Calendar.MONTH) +1 );
		str = str + "/" + String.valueOf( gc.get(Calendar.YEAR) );
		return str;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdProfessor() {
		return idProfessor;
	}
	public void setIdProfessor(int idProfessor) {
		this.idProfessor = idProfessor;
	}
	public Titulo getT() {
		return t;
	}
	public void setT(Titulo t) {
		this.t = t;
	}
}
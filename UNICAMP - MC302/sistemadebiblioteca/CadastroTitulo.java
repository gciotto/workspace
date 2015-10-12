package sistemadebiblioteca;

import java.util.ArrayList;
public class CadastroTitulo {
	private ArrayList<Titulo> listaTitulos;
	
	private static int quant_exemplares = 0;
	private static int quant_livros = 0;
	private static int quant_midias = 0;
	
	public CadastroTitulo(){
		
		Titulo t1 =  FisicoFactory.getInstance().newLivro("Dom Camurro",0 ,5 , "Machado de Assis");
		t1.adicionarExemplar(new Exemplar(0, t1));
		quant_livros = 1;
		
		Titulo t2 = FisicoFactory.getInstance().newTitulo(1,"Bras Cubas");
		t2.adicionarExemplar(new Exemplar(0, t2));
		t2.adicionarExemplar(new Exemplar(1, t2));
		t2.adicionarExemplar(new Exemplar(2, t2));
		
		Titulo t3 = FisicoFactory.getInstance().newTitulo(2,"Macunaima");
		t3.adicionarExemplar(new Exemplar(0, t3));
		t3.adicionarExemplar(new Exemplar(1, t3));
		t3.adicionarExemplar(new Exemplar(2, t3));
		
		listaTitulos = new ArrayList<Titulo>();
		quant_exemplares = 7;
		listaTitulos.add(t1);
		listaTitulos.add(t2);
		listaTitulos.add(t3);
	}
	
	public static int getQuant_exemplares() {
		return quant_exemplares;
	}
	public static int getQuant_livros() {
		return quant_livros;
	}
	public static int getQuant_midias() {
		return quant_midias;
	}
	public ArrayList<Titulo> getListaTitulos() {
		return listaTitulos;
	}
	public void setListaExemplares(ArrayList<Titulo> listaTitulo) {
		this.listaTitulos = listaTitulo;
	}
	public void cadastrar(Titulo t, int quant) {
			listaTitulos.add(t);
			// adiciona a quantidade de exemplares passada como parametro.
			for (int i = 0; i < quant; i++) {
				t.adicionarExemplar(new Exemplar(i,t));
				quant_exemplares++;
				if (t instanceof Livro) quant_livros++;
				if (t instanceof Midia) quant_midias++;
			}
	}
}
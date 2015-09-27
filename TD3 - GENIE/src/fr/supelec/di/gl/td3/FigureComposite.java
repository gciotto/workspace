package fr.supelec.di.gl.td3;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.Vector;

public class FigureComposite extends Figure {

	private Vector<Figure> children;
	
	public FigureComposite(Vector<Figure> c) {
		super(0,0);
		this.children = c;
	}
		
	public void dessiner(Graphics g) {
		
		for (Figure f : children)
			f.dessiner(g);

	}

	public boolean estSousSouris(int sx, int sy) {
		
		for (Figure f : children)
			if (f.estSousSouris(sx, sy))
				return true;
		
		return false;
	}

	public Iterator<Figure> enfants() {
		
		return this.children.iterator();
	}

	public void selectionner() {
		
		super.selectionner();
		for (Figure f : children)
			f.selectionner();		
	}

	public void deselectionner() {
		
		super.deselectionner();
		for (Figure f : children)
			f.deselectionner();
	}
	
	public void deplacer(int dx, int dy) {
		
		super.deplacer(dx, dy);
		for (Figure f : children)
			f.deplacer(dx, dy);		
	}

	
	public Figure cloner() {
		
		Vector<Figure> v = new Vector<Figure>();
		
		for (Figure f : this.children)
			v.add(f.cloner());
		
		return new FigureComposite(v);
	}

}

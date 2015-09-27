/* -*-Java-*-
 * ###################################################################
 * 
 *  FILE: "FigAbstrait.java"
 *                                    created: 2002-10-11 
 *                
 *  Author: Bich-Liên Doan
 *  E-mail: doan@supelec.fr
 *  
 *  Description: 
 * 
 *  History
 * 
 *  modified   by  rev reason
 *  ---------- --- --- -----------
 *  2009-06-05 DMA     adapted to Eclipse and cleaned according to my taste
 *
 * ###################################################################
 */

package fr.supelec.di.gl.td3;

import java.awt.Graphics;
import java.awt.Point;

public abstract class Figure  {

	protected int x, y;
	protected boolean sel;

	
	protected Figure(int x, int y) {
		this.x = x;
		this.y = y;

		this.sel = false;
	}

	
	abstract public Figure cloner();
	
	abstract public void dessiner(Graphics g);

	abstract public boolean estSousSouris(int sx, int sy);

	public boolean estSelectionne() {
		return this.sel;
	}

	public void selectionner() {
		this.sel = true;
	}

	public void deselectionner() {
		this.sel = false;
	}

	public void deplacer(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}

	public Point position() {
		return new Point(this.x, this.y);
	}

}

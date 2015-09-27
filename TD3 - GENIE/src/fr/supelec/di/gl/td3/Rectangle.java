/* -*-Java-*-
 * ###################################################################
 * 
 *  FILE: "FigRectangle.java"
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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;

public class Rectangle extends Figure {

	private int w, h;

	public Rectangle(int x, int y, int w, int h) {
		super(x, y);
		this.w = w;
		this.h = h;
	}

	@Override
	public void dessiner(Graphics g) {
		if (this.sel == true)
			g.setColor(Color.red);
		else
			g.setColor(Color.black);

		g.drawRect(this.x, this.y, this.w, this.h);
	}

	@Override
	public boolean estSousSouris(int sx, int sy) {
		Line2D line;
		java.awt.Rectangle rect = new java.awt.Rectangle(sx - 5, sy - 5, 10, 10);

		line = new Line2D.Double(this.x, this.y, this.x + this.w, this.y);
		if (line.intersects(rect))
			return true;
		line = new Line2D.Double(this.x, this.y + this.h, this.x + this.w, this.y + this.h);
		if (line.intersects(rect))
			return true;
		line = new Line2D.Double(this.x, this.y, this.x, this.y + this.h);
		if (line.intersects(rect))
			return true;
		line = new Line2D.Double(this.x + this.w, this.y, this.x + this.w, this.y + this.h);
		if (line.intersects(rect))
			return true;
		return false;
	}

	
	public Figure cloner() {
		
		return new Rectangle(x, y, w, h);
	}

}

/* -*-Java-*-
 * ###################################################################
 * 
 *  FILE: "FigLosange.java"
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

public class Losange extends Figure {

	private int w, h;

	public Losange(int x, int y, int w, int h) {
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

		g.drawLine(this.x, this.y + this.h / 2, this.x + this.w / 2, this.y);
		g.drawLine(this.x + this.w / 2, this.y, this.x + this.w, this.y + this.h / 2);
		g.drawLine(this.x, this.y + this.h / 2, this.x + this.w / 2, this.y + this.h);
		g.drawLine(this.x + this.w / 2, this.y + this.h, this.x + this.w, this.y + this.h / 2);
	}

	@Override
	public boolean estSousSouris(int sx, int sy) {
		Line2D line;
		java.awt.Rectangle rect = new java.awt.Rectangle(sx - 5, sy - 5, 10, 10);
		line = new Line2D.Double(this.x, this.y + this.h / 2, this.x + this.w / 2, this.y);
		if (line.intersects(rect))
			return true;
		line = new Line2D.Double(this.x + this.w / 2, this.y, this.x + this.w, this.y + this.h / 2);
		if (line.intersects(rect))
			return true;
		line = new Line2D.Double(this.x, this.y + this.h / 2, this.x + this.w / 2, this.y + this.h);
		if (line.intersects(rect))
			return true;
		line = new Line2D.Double(this.x + this.w / 2, this.y + this.h, this.x + this.w, this.y + this.h / 2);
		if (line.intersects(rect))
			return true;
		return false;
	}

	@Override
	public Figure cloner() {
		
		return new Losange(x, y, w, h);
	}

}

/* -*-Java-*-
 * ###################################################################
 * 
 *  FILE: "ZoneDessin.java"
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
 *  2009-05-29 DMA     adapted to Eclipse and cleaned according to my taste
 *  
 * ###################################################################
 */

package fr.supelec.di.gl.td3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.util.Iterator;
import java.util.Vector;

@SuppressWarnings("serial")
public class ZoneDessin extends JPanel implements ActionListener, MouseListener {

	// Grouper l'ensemble des figures sélectionnées.
	public void operationGrouper() {
		/***************************************************************************
		 * A_FAIRE1 : décommenter les instructions suivantes pour la première question
		 * Pour cela, sélectionner le bloc de lignes ci-dessous préfixées par //
		 * et choisir Source -> Toggle Comment
		 * Faire de même pour la méthode suivante
		 ***************************************************************************/
		Vector<Figure> v = obtenirFiguresSelectionnees();
		if (v.size() < 2) {
			JOptionPane.showMessageDialog(null,
				"Il faut sélectionner plusieurs figures avant de les grouper");
			return;
		}
		Iterator<Figure> it = v.iterator();
		while (it.hasNext()) {
			this.figures.remove(it.next());
		}

		Figure fig = new FigureComposite(v);
		this.figures.add(fig);
		fig.deselectionner();
		repaint(100);
	}

	// dégrouper la figure sélectionnée
	public void operationDegrouper() {
		/***************************************************************************
		 * A_FAIRE1 : décommenter les instructions suivantes pour la première question
		 ***************************************************************************/
		Figure fig = obtenirLaFigureSelectionnee();
		if (fig != null) {
			if (! (fig instanceof FigureComposite)) {
			JOptionPane.showMessageDialog(null,
					"Vous ne pouvez dégrouper qu'une figure composite ");
				return;
			}
		} else
			return;

		this.figures.remove(fig);
		FigureComposite figc = (FigureComposite) fig;
		Iterator<Figure> it = figc.enfants();
		while (it.hasNext()) {
			Figure f = it.next();
			f.deselectionner();
			this.figures.add(f);
		}
		repaint(100);
	}

	// copier la figure sélectionnée dans le presse papier
	public void operationCopier() {
		/***************************************************************************
		 * A_FAIRE2 : décommenter les instructions suivantes pour la seconde question
		 ***************************************************************************/
		Figure fig = obtenirLaFigureSelectionnee();
		if (fig == null)
			return;

		this.pressePapier = fig.cloner();
	}

	// coller le contenu du presse papier dans la zone de dessin.
	// On clone la figure puis après récupération de sa nouvvelle
	// position et taille, elle est insérée dans la zone de dessin.
	public void operationColler() {
		/***************************************************************************
		 * A_FAIRE2 : décommenter les instructions suivantes pour la seconde question
		 ***************************************************************************/
		if (this.pressePapier == null) {
			JOptionPane.showMessageDialog(null, "Le presse papier est vide");
			return;
		}

		Figure fig = this.pressePapier.cloner();

		DialogueDeplacementFigure cfg = new DialogueDeplacementFigure(null,
				"Nouvelle position : ", fig.position().x, fig.position().y);
		cfg.pack();
		cfg.setVisible(true);

		if (cfg.okButton()) {
			int dx = cfg.getX() - fig.position().x;
			int dy = cfg.getY() - fig.position().y;
			fig.deplacer(dx, dy);
			this.figures.add(fig);
			repaint(100);
		}
	}

	// liste des figures de la zone de dessin.
	private Vector<Figure> figures;

	// figure se trouvant dans le presse papier
	// ou "null" si aucune figure n'y est présente.
	private Figure pressePapier;

	public ZoneDessin() {
		super();

		this.figures = new Vector<Figure>();
		this.pressePapier = null;

		setPreferredSize(new Dimension(600, 300));

		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder border = BorderFactory.createTitledBorder(blackline,
				"Zone de Dessin");
		border.setTitleJustification(TitledBorder.CENTER);

		setBorder(border);
		addMouseListener(this);
	}

	// crée une nouvelle figure et l'insère dans la zone de dessin.
	// la boite de dialoge "CreationFigDialog" sert à récupérer la taille
	// et la position de la figure à créer.
	public void operationCreerFigure(String nom) {
		DialogueCreationFigure cfg = new DialogueCreationFigure(null,
				"Création Figure : " + nom, 50, 50, 50, 50);
		cfg.pack();
		cfg.setVisible(true);

		if (cfg.okButton()) {
			int x = cfg.getX();
			int y = cfg.getY();
			int w = cfg.getW();
			int h = cfg.getH();
			if (nom.equals("Rectangle")) {
				this.figures.add(new Rectangle(x, y, w, h));
			} else {
				this.figures.add(new Losange(x, y, w, h));
			}
			repaint(100);
		}
	}

	// sélectionner toutes les figures
	public void operationToutSelectionner() {
		Iterator<Figure> it = this.figures.iterator();
		while (it.hasNext()) {
			Figure fig = (Figure) it.next();
			fig.selectionner();
		}
		repaint();
	}

	// déselectionner toutes les figures
	public void operationToutDeselectionner() {
		Iterator<Figure> it = this.figures.iterator();
		while (it.hasNext()) {
			Figure fig = (Figure) it.next();
			fig.deselectionner();
		}
		repaint();
	}

	// déplacer la figure sélectionnée.
	public void operationDeplacer() {
		Figure fig = obtenirLaFigureSelectionnee();
		if (fig == null)
			return;

		DialogueDeplacementFigure cfg = new DialogueDeplacementFigure(null,
				"Deplacer Figure : ", fig.position().x, fig.position().y);
		cfg.pack();
		cfg.setVisible(true);

		if (cfg.okButton()) {
			int x = cfg.getX();
			int y = cfg.getY();
			Point p = fig.position();
			int dx = x - p.x;
			int dy = y - p.y;
			fig.deplacer(dx, dy);
			repaint();
		}

	}

	public Vector<Figure> obtenirFiguresSelectionnees() {
		Vector<Figure> sel = new Vector<Figure>();
		Iterator<Figure> it = this.figures.iterator();
		while (it.hasNext()) {
			Figure fig = (Figure) it.next();
			if (fig.estSelectionne())
				sel.add(fig);
		}
		return sel;
	}

	public Figure obtenirLaFigureSelectionnee() {
		Vector<Figure> sel = obtenirFiguresSelectionnees();
		if (sel.size() > 1) {
			JOptionPane.showMessageDialog(null,
					"Il y a plus d'une figure sélectionnées");
			return null;
		}
		if (sel.size() == 0) {
			JOptionPane.showMessageDialog(null,
					"Il y a aucune figure sélectionnée");
			return null;
		}
		Figure fig = (Figure) sel.elementAt(0);
		return fig;
	}

	// Cette méthode est appelée à chaque fois qu'une entrée du menu
	// est sélectionnée. elle "dispatche" alors la commande sur l'une
	// des méthodes "opCopier", "opColler", ... en fonction de l'entrée
	// du menu sélectionnée.
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
	
		if (cmd.equals("Rectangle") || cmd.equals("Losange")) {
			operationCreerFigure(cmd);
			return;
		}
	
		if (cmd.equals("Copier")) {
			operationCopier();
			return;
		}
		if (cmd.equals("Coller")) {
			operationColler();
			return;
		}
		if (cmd.equals("Grouper")) {
			operationGrouper();
			return;
		}
		if (cmd.equals("Dégrouper")) {
			operationDegrouper();
			return;
		}
		if (cmd.equals("Tout sélectionner")) {
			operationToutSelectionner();
			return;
		}
		if (cmd.equals("Tout désélectionner")) {
			operationToutDeselectionner();
			return;
		}
		if (cmd.equals("Déplacer")) {
			operationDeplacer();
			return;
		}
		if (cmd.equals("Quitter")) {
			System.exit(0);
		}
	
		System.err.println("Commande inconnue !");
	}

	// réaffiche le contenu de la zone de dessin. pour cela nous
	// passons en revue l'ensemble des figures de la zone de dessin
	// puis nous appellons la méthode "dessiner" de chacune de ces
	// figures.
	protected void paintComponent(Graphics g) {
		Iterator<Figure> it = this.figures.iterator();
		java.awt.Rectangle r = g.getClipBounds();
		((Graphics2D) g).setBackground(Color.white);
		g.clearRect(r.x, r.y, r.width, r.height);
	
		while (it.hasNext()) {
			Figure fig = (Figure) it.next();
			fig.dessiner(g);
		}
	}

	// Interception d'un click souris dans la zone de dessin.
	// Si une figure est sous la souris, elle devient sélectionnée
	// ou déselectionnée selon son état précédent.
	public void mouseClicked(MouseEvent e) {
		Figure selFig = null;
		Iterator<Figure> it = this.figures.iterator();
		while ((selFig == null) && (it.hasNext())) {
			Figure fig = (Figure) it.next();
			if (fig.estSousSouris(e.getX(), e.getY())) {
				selFig = fig;
				break;
			}
		}
		if (selFig != null) {
			if (selFig.estSelectionne()) {
				selFig.deselectionner();
			} else {
				selFig.selectionner();
			}
			repaint();
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}

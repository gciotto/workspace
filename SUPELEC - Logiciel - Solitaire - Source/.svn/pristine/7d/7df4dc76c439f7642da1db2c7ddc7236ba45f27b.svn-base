package interfaces;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import controleurs.ControleurJeu;
import entites.*;

public class JPlateau extends JPanel {
	
	private int rows, cols;
	private JCase[][] cases;
	private GridLayout grelha;
	
	public JPlateau (Plateau p) {
		
		super();
		
		this.rows = p.getNbLignes();
		this.cols = p.getNbColonnes();
		
		grelha = new GridLayout (rows, cols);
		this.setLayout(grelha);
		
		cases = new JCase[rows][cols];
		
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				
				int etat = p.getCaseA(i, j).getEtat();
				cases [i][j] = new JCase(etat);
				
				this.add(cases[i][j]);
			}
		
	}
	
	public void paintComponent (Graphics g) {
		
		super.paintComponent(g);
		
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				cases[i][j].paintComponents(g);
			}
	}

	public JCase[][] getCases() {
	 	 return cases; 
	}

	public void actualiserPlateau(Plateau p) { 
		
		int r = p.getNbLignes(),
			c = p.getNbColonnes();
		
		if (this.rows != r || this.cols != c) {
						
			this.invalidate();
			
			this.grelha.setRows(r);
			this.grelha.setColumns(c);
			
			this.removeAll();
						
			this.rows = r;
			this.cols = c;
			this.cases = new JCase [r][c];
			
			for (int i = 0; i < rows; i++)
				for (int j = 0; j < cols; j++) {
					
					cases[i][j] = new JCase (p.getCaseA(i, j).getEtat());
					this.add(cases[i][j]);
				}
			
		} else {
			
			for (int i = 0; i < rows; i++)
				for (int j = 0; j < cols; j++)
					cases[i][j].changeEtat(p.getCaseA(i, j).getEtat());
				
		}
					
	 }

	public void actualiserPlateau(List<Coup> coups) {
		
		for (Coup c : coups)
			this.actualiserPlateau(c);
	}
	
	public void actualiserPlateau(Coup c) { 
		
		int lOrigine = c.getCoordonneeOrigine().getLigne(),
			cOrigine = c.getCoordonneeOrigine().getColonne(),
			lNouvelle = 0, cNouvelle = 0;
		
		this.cases[lOrigine][cOrigine].changeEtat(Case.VIDE);
		
		switch (c.getDirection().getDirectionCode()) {
		
		case Direction.BAS 		: 	lOrigine++;
									lNouvelle = lOrigine + 1;
									cNouvelle = cOrigine;
									break;
		case Direction.HAUT 	: 	lOrigine--;
									lNouvelle = lOrigine - 1;
									cNouvelle = cOrigine;
									break;
		case Direction.DROITE	: 	cOrigine++;
									cNouvelle = cOrigine + 1;
									lNouvelle = lOrigine;
									break;
		case Direction.GAUCHE	: 	cOrigine--;
									cNouvelle = cOrigine - 1;
									lNouvelle = lOrigine;
									break;		
		}
		
		this.cases[lNouvelle][cNouvelle].changeEtat(Case.OCCUPEE);
		this.cases[lOrigine][cOrigine].changeEtat(Case.VIDE);

	 } 

}

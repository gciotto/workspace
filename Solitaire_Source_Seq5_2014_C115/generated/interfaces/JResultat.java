package interfaces;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import entites.*;

public class JResultat extends JPanel {

	private JTextArea coups, coupsPossibles;
	private JButton buttonProchain, buttonFinir;
	private JRadioButton min, max, dMax, dMin, back, concentre, branchbound, melange, listeAleatoire, niveauBB;
	private JTextField nbNiveau;

	public JResultat () {

		super();

		coups = new JTextArea();
		coups.setEditable(false);

		coupsPossibles = new JTextArea();
		coupsPossibles.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(coups);
		JScrollPane scrollPanePossibles = new JScrollPane(coupsPossibles);

		JPanel auxSuperieur = new JPanel ();
		auxSuperieur.setLayout(new BorderLayout());
		
		JPanel auxInferieur = new JPanel ();
		auxInferieur.setLayout(new BorderLayout());
		
		JLabel label = new JLabel ("Liste de Coups:");

		auxSuperieur.add(label, BorderLayout.NORTH);
		auxSuperieur.add(scrollPane, BorderLayout.CENTER);
		
		buttonProchain = new JButton ("Prochain Coup");
		buttonProchain.setEnabled(false);
		
		buttonFinir = new JButton ("Finir Jeu");
		buttonFinir.setEnabled(false);
		
		JPanel buttons = new JPanel (new GridLayout(2,1));
		buttons.add(buttonProchain);
		buttons.add(buttonFinir);

		auxInferieur.add (new JLabel ("Liste de Coups Possibles:"), BorderLayout.NORTH);
		auxInferieur.add(scrollPanePossibles, BorderLayout.CENTER);
		auxInferieur.add(buttons, BorderLayout.SOUTH);
		
		JPanel radiobtn = new JPanel(new GridLayout(11,1));
		
		max = new JRadioButton("Nb. max. de voisins occupés");
		max.setSelected(true);
		
		min = new JRadioButton("Nb. min. de voisins occupés");
		min.setSelected(false);
		
		dMin = new JRadioButton("Dist. min.");
		dMin.setSelected(false);
		
		dMax = new JRadioButton("Dist. max.");
		dMax.setSelected(false);
		
		back = new JRadioButton("Meilleure solution");
		back.setSelected(false);
		
		concentre = new JRadioButton("Vers le centre");
		concentre.setSelected(false);
		
		branchbound = new JRadioButton("Branch and Bound");
		branchbound.setSelected(false);
		
		melange = new JRadioButton("Méthode mélangée");
		melange.setSelected(false);
		
		listeAleatoire = new JRadioButton("Plusieurs Branch Bound");
		listeAleatoire.setSelected(false);
		
		niveauBB = new JRadioButton("Branch Bound avec Niveau");
		niveauBB.setSelected(false);
		nbNiveau = new JTextField(3);
		
		JPanel pNiveau = new JPanel();
		pNiveau.setLayout(new BorderLayout());
		pNiveau.add(niveauBB, BorderLayout.CENTER);
		pNiveau.add(nbNiveau, BorderLayout.EAST);
		
		ButtonGroup btnGroupe = new ButtonGroup();
		btnGroupe.add(max);
		btnGroupe.add(dMax);
		btnGroupe.add(concentre);
		btnGroupe.add(dMin);
		btnGroupe.add(min);
		btnGroupe.add(back);
		btnGroupe.add(branchbound);
		btnGroupe.add(melange);
		btnGroupe.add(listeAleatoire);
		btnGroupe.add(niveauBB);
		
		this.setLayout(new GridLayout(3,1));

		radiobtn.add(new JLabel("Algorithmes disponibles:"));
		radiobtn.add(max);
		radiobtn.add(min);
		radiobtn.add(dMax);
		radiobtn.add(dMin);
		radiobtn.add(back);
		radiobtn.add(concentre);
		radiobtn.add(branchbound);
		radiobtn.add(melange);
		radiobtn.add(listeAleatoire);
		radiobtn.add(pNiveau);
		
		
		this.add(radiobtn);
		this.add(auxSuperieur);
		
		this.add(auxInferieur);

	}

	public JButton getButtonProchain() {
		return buttonProchain;
	}
	
	public JButton getButtonFinir() {
		return buttonFinir;
	}

	public void afficherNouveauxCoups(List<Coup> coups) { 

		for (Coup c : coups)
			this.coups.append(c.toString() + "\n");
	}
	
	public void afficherNouveauCoup(Coup c) { 

		this.coups.append(c.toString() + "\n");
	} 

	public void reinitialiser() { 

		this.coups.setText("");
		this.coupsPossibles.setText("");
		this.buttonProchain.setEnabled(true);
		this.buttonFinir.setEnabled(true);
	}

	public void afficherCoupsPossibles(List<Coup> cPossibles) {
		
		for (Coup c : cPossibles) {
			
			this.coupsPossibles.append(c.toString() + "\n");
			
		}
		
	}

	public void ajouterCoup(Coup prochain) {
		
		this.coups.append(prochain.toString() + "\n");
		
	}

	public void finirJeu() {
		this.buttonProchain.setEnabled(false);
		this.buttonFinir.setEnabled(false);
	}

	public void reinitialiserProchainsCoups() {
		this.coupsPossibles.setText("");
		
	} 

	public boolean isMaxVoisin() {
		return this.max.isSelected();
	}
	
	public boolean isMinVoisin() {
		return this.min.isSelected();
	}
	
	public boolean isDistanceMaxVoisin() {
		return this.dMax.isSelected();
	}
	
	public boolean isDistanceMinVoisin() {
		return this.dMin.isSelected();
	}
	
	public boolean isOptimale() {
		return this.back.isSelected();
	}
	
	public boolean isHeurestique() {
		return this.concentre.isSelected();
	}
	
	public boolean isBranchAndBound() {
		return this.branchbound.isSelected();
	}
	
	public boolean isMelange() {
		return this.melange.isSelected();
	}

	
	public boolean isListeAleatoire() {
		return this.listeAleatoire.isSelected();
	}

	public boolean isNiveau() {
		return this.niveauBB.isSelected();
	}

	public int getNiveau() {
		return Integer.valueOf(this.nbNiveau.getText());
	}

	
}

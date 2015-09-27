package interfaces;

import controleurs.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;

import entites.*;

public class JSolitaire  extends JFrame implements ActionListener, IControleurInterface {


	private JMenuItem entree, sortie, about, lireCoups;
	private JPlateau plateau;
	private JResultat resultats;
	private ControleurJeu controleurJeu;
	private java.util.List<Coup> optimales;

	private final JFileChooser fc = new JFileChooser();

	public JSolitaire () {

		super("Jeu du Solitaire");

		Container contentPane = this.getContentPane();


		plateau = new JPlateau(ControleurJeu.plateauVide());
		resultats = new JResultat();

		contentPane.setLayout(new BorderLayout());

		contentPane.add(plateau, BorderLayout.CENTER);
		contentPane.add(resultats, BorderLayout.EAST);


		setPreferredSize(new Dimension (670, 600));
		//this.setResizable(false);

		JMenuBar bar = new JMenuBar();
		JMenu io = new JMenu ("Opérations");

		sortie = new JMenuItem("Ecrite de Fichier");
		sortie.addActionListener(this);

		entree = new JMenuItem("Lecture de Fichier");
		entree.addActionListener(this);

		lireCoups = new JMenuItem("Lire Coups");
		lireCoups.addActionListener(this);
		
		about = new JMenuItem("Développeurs");
		about.addActionListener(this);

		io.add(entree);
		io.add(sortie);
		io.add(lireCoups);
		io.add(about);

		bar.add(io);
		setJMenuBar(bar);

		this.resultats.getButtonProchain().addActionListener(this);
		this.resultats.getButtonFinir().addActionListener(this);

		this.setVisible(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

	public JSolitaire (ControleurJeu c) {
		this();
		this.controleurJeu = c;
	}


	public void changeControleur (ControleurJeu nouveauControleur) {
		this.controleurJeu = nouveauControleur;
	}


	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == about)
			JOptionPane.showMessageDialog(this, "Projet Logiciel - Supelec 2014 \nGustavo CIOTTO PINTON\nIgnacio GARCIA ATANCE","Développeurs",
					JOptionPane.INFORMATION_MESSAGE);

		else if (e.getSource() == this.entree || e.getSource() == this.sortie || e.getSource() == this.lireCoups){

			int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				try {
					File file = fc.getSelectedFile();

					if (e.getSource() == this.entree) {

						Fichier f = new Fichier (file);

						this.invalidate();
						
						optimales = null;
						Plateau p = controleurJeu.lirePlateau(f);
						this.plateau.actualiserPlateau(p);

						this.resultats.reinitialiser();

						java.util.List<Coup> prochains = this.controleurJeu.coupsPossibles();

						this.resultats.afficherCoupsPossibles(this.controleurJeu.coupsPossibles());

						this.validate();
						this.repaint();

						if (prochains.isEmpty())
							this.finirOperation();


					}
					else if (e.getSource() == this.sortie) {

						Fichier f = new Fichier (file);

						boolean b = this.controleurJeu.ecrireListeCoups(f);

						if (b) 	JOptionPane.showMessageDialog(this, "L'opération d'écrite a réussi.",
								"Ecrite réussite",
								JOptionPane.DEFAULT_OPTION);

					}
					else if (e.getSource() == this.lireCoups) {
						
						Fichier f = new Fichier (file);

						this.resultats.reinitialiser();
						
						java.util.List<Coup> coups = this.controleurJeu.lireCoups(f);
						
						this.plateau.actualiserPlateau(coups);
						this.resultats.afficherNouveauxCoups(coups);
						
						java.util.List<Coup> prochains = this.controleurJeu.coupsPossibles();

						this.resultats.afficherCoupsPossibles(prochains);

						this.validate();
						this.repaint();

						if (prochains.isEmpty())
							this.finirOperation();

						
					}
				} catch (IOException e1) {

					JOptionPane.showMessageDialog(this, "L'opération a échoué: \n"+ e1.getMessage(),
							"Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else if (e.getSource() == this.resultats.getButtonProchain()) {

			Coup prochain = this.deciderAlgorithme();

			this.plateau.actualiserPlateau(prochain);
			this.resultats.ajouterCoup(prochain);

			this.resultats.reinitialiserProchainsCoups();

			java.util.List<Coup> prochains = this.controleurJeu.coupsPossibles();

			this.resultats.afficherCoupsPossibles(prochains);

			this.repaint();

			if (prochains.isEmpty())
				this.finirOperation();

		} else if (e.getSource() == this.resultats.getButtonFinir()) {

			this.resultats.finirJeu();
			
			this.resultats.reinitialiserProchainsCoups();

			if (!this.isResultatListe()) {
				
				Coup prochain = this.deciderAlgorithme();		 

				while (prochain != null) {

					this.plateau.actualiserPlateau(prochain);
					this.resultats.ajouterCoup(prochain);

					prochain = this.deciderAlgorithme();
				}
			}
			else {
				
				java.util.List<Coup> coupsOptimales;
				if (this.resultats.isOptimale())
					coupsOptimales = this.controleurJeu.prochaineListeOptimale(true, new BooleanResult(false));
				else if (this.resultats.isListeAleatoire())
					coupsOptimales = this.controleurJeu.prochaineListePlusieursBranchBound();
				else if (this.resultats.isMelange())
					coupsOptimales = this.controleurJeu.prochaineListeMelangee();
				else if (this.resultats.isBranchAndBound())
					coupsOptimales = this.controleurJeu.prochaineBranchBoundList();
				else {
					java.util.List<Coup> precedent = new ArrayList<Coup>();
					precedent.addAll(this.controleurJeu.getListeCoups());
					coupsOptimales = this.controleurJeu.prochainNiveauBranchAndBound(precedent,this.resultats.getNiveau());
				}
				
				
				for (Coup c : coupsOptimales) {
					
					this.plateau.actualiserPlateau(c);
					this.resultats.ajouterCoup(c);
					
					this.controleurJeu.ajouterCoup(c);
				}
				
			}
			this.validate();
			this.repaint();

			this.resultats.afficherCoupsPossibles(this.controleurJeu.coupsPossibles());
			this.finirOperation();
		}

	}	

	private boolean isResultatListe() {
		return (this.resultats.isOptimale() || this.resultats.isListeAleatoire()
				|| this.resultats.isMelange() || this.resultats.isBranchAndBound() 
				|| this.resultats.isNiveau());
	}

	private Coup deciderAlgorithme() {

		Coup prochain = null;

		if (!this.isResultatListe())
			optimales = null;
		
		if (this.resultats.isMinVoisin())
			prochain = this.controleurJeu.prochainMinVoisinCoup();
		
		else if (this.resultats.isMaxVoisin())
			prochain = this.controleurJeu.prochainMaxVoisinCoup();
		
		else if (this.resultats.isDistanceMaxVoisin())
			prochain = this.controleurJeu.prochainDistanceMaxVoisinCoup();
		
		else if (this.resultats.isDistanceMinVoisin())
			prochain = this.controleurJeu.prochainDistanceMinVoisinCoup();		
		/*else if (this.resultats.isMelange())
			prochain = this.controleurJeu.prochainMelange();*/
		
		else if (this.isResultatListe()){
			
			if (this.optimales == null) {
				if (this.resultats.isOptimale())
					optimales =  this.controleurJeu.prochaineListeOptimale(true, new BooleanResult (false));
				else if (this.resultats.isListeAleatoire())
					optimales =  this.controleurJeu.prochaineListePlusieursBranchBound();
				else if (this.resultats.isMelange())
					optimales =  this.controleurJeu.prochaineListeMelangee();
				else if (this.resultats.isBranchAndBound())
					optimales =  this.controleurJeu.prochaineBranchBoundList();
				else if (this.resultats.isNiveau()) {
					java.util.List<Coup> precedent = new ArrayList<Coup>();
					precedent.addAll(this.controleurJeu.getListeCoups());
					optimales =  this.controleurJeu.prochainNiveauBranchAndBound(precedent,this.resultats.getNiveau());
				}
			}
			
			prochain = this.optimales.remove(0);
			
		} else prochain =  this.controleurJeu.prochainCoupVersLeCentre();	

		if (prochain != null)
			this.controleurJeu.ajouterCoup(prochain);
		
		return prochain;
	}

	public JPlateau getPlateau() {
		return plateau; 
	}

	public JResultat getPlateauResultat() {
		return resultats; 
	}

	public void finirOperation() { 
		this.resultats.finirJeu();
		JOptionPane.showMessageDialog(this, "Plus de movements possibles.\nTotal: "+
				this.controleurJeu.getTotalMovements()+
				"\nPièces restantes: " + this.controleurJeu.getPiecesRestantes(),
				"Fin",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void metEnOperation() { 
		this.setVisible(true);
	} 

}

package interfaces;

import controleurs.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import entites.*;

public class JSolitaire  extends JFrame implements ActionListener, IControleurInterface {


	private JMenuItem entree, sortie, about;
	private JPlateau plateau;
	private JResultat resultats;

	private ControleurJeu controleurJeu;

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
		this.setResizable(false);

		JMenuBar bar = new JMenuBar();
		JMenu io = new JMenu ("Opérations");

		sortie = new JMenuItem("Ecrite de Fichier");
		sortie.addActionListener(this);

		entree = new JMenuItem("Lecture de Fichier");
		entree.addActionListener(this);

		about = new JMenuItem("Développeurs");
		about.addActionListener(this);

		io.add(entree);
		io.add(sortie);
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

		else if (e.getSource() == this.entree || e.getSource() == this.sortie){

			int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				try {
					File file = fc.getSelectedFile();

					if (e.getSource() == this.entree) {

						Fichier f = new Fichier (file);

						this.invalidate();

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
				} catch (IOException e1) {

					JOptionPane.showMessageDialog(this, "L'opération a échoué: \n"+ e1.getMessage(),
							"Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else if (e.getSource() == this.resultats.getButtonProchain()) {

			//Coup prochain = this.controleurJeu.prochainCoup();
			//Coup prochain = this.controleurJeu.prochainRandomCoup();
			//Coup prochain = this.controleurJeu.prochainMinVoisinCoup();

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
			this.invalidate();
			this.resultats.reinitialiserProchainsCoups();

			if (!this.resultats.isOptimale()) {
				Coup prochain = this.deciderAlgorithme();		 

				while (prochain != null) {

					this.plateau.actualiserPlateau(prochain);
					this.resultats.ajouterCoup(prochain);

					prochain = this.deciderAlgorithme();
				}
			}
			else {
				
				java.util.List<Coup> coupsOptimales = this.controleurJeu.prochainOptimalCoup(); 
				
				for (Coup c : coupsOptimales) {
					
					this.plateau.actualiserPlateau(c);
					this.resultats.ajouterCoup(c);
					
					this.controleurJeu.ajouterCoup(c);
				}
				
			}
			this.validate();
			this.repaint();

			this.finirOperation();
		}

	}	

	private Coup deciderAlgorithme() {

		Coup prochain = null;

		if (this.resultats.isMinVoisin())
			prochain = this.controleurJeu.prochainMinVoisinCoup();
		else if (this.resultats.isMaxVoisin())
			prochain = this.controleurJeu.prochainMaxVoisinCoup();
		else if (this.resultats.isDistanceMaxVoisin())
			prochain = this.controleurJeu.prochainDistanceMaxVoisinCoup();
		else if (this.resultats.isDistanceMinVoisin())
			prochain = this.controleurJeu.prochainDistanceMinVoisinCoup();
		else {
			prochain =  this.controleurJeu.prochainOptimalCoup().get(0);
			this.controleurJeu.ajouterCoup(prochain);
		}
		//else prochain = this.controleurJeu.prochainRandomCoup();	

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

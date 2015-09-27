import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;


public class JSolitaire extends JFrame implements ActionListener{

	private JMenuItem entree, sortie, about;
	private final JFileChooser fc = new JFileChooser();

	public JSolitaire () {

		super("Jeu du Solitaire");

		Container contentPane = this.getContentPane();

		char[][] matrice = { 	{ '#' , '#', '#', '1', '1', '1','#' , '#', '#' }, 
				{ '#' , '#', '#', '1', '1', '1','#' , '#', '#' },
				{ '#' , '#', '#', '1', '1', '1','#' , '#', '#' },
				{ '1' , '1', '1', '1', '1', '1','1' , '1', '1' },
				{ '1' , '1', '1', '1', '0', '1','1' , '1', '1' },
				{ '1' , '1', '1', '1', '1', '1','1' , '1', '1' },
				{ '#' , '#', '#', '1', '1', '1','#' , '#', '#' },
				{ '#' , '#', '#', '1', '1', '1','#' , '#', '#' }, 
				{ '#' , '#', '#', '1', '1', '1','#' , '#', '#' },};

		JTableau tableau = new JTableau(matrice);
		JResultat resultats = new JResultat();

		contentPane.setLayout(new BorderLayout());

		contentPane.add(tableau);
		contentPane.add(resultats, BorderLayout.EAST);
		//tableau.setVisible(false);

		setPreferredSize(new Dimension (600, 550));

		JMenuBar bar = new JMenuBar();
		JMenu io = new JMenu ("Opérations");

		sortie = new JMenuItem("Ecriture de Fichier");
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

		pack();
	}


	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == about)
			JOptionPane.showMessageDialog(this, "Projet Logiciel - Supelec 2014 \nGustavo CIOTTO PINTON\nIgnacio GARCIA ATANCE","Développeurs",
					JOptionPane.INFORMATION_MESSAGE);

		else {
			int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File file = fc.getSelectedFile();

				if (e.getSource() == this.entree)
					System.out.println("coco");
				else if (e.getSource() == this.sortie)
					System.out.println("xixi");
			}
		}


	}	

}

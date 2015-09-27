/* -*-Java-*-
 * ###################################################################
 * 
 *  FILE: "ApplicationDessin.java"
 *                                    created: 2002-10-11 
 *                
 *  Author: Bich-Liên Doan
 *  E-mail: doan@supelec.fr
 *  
 *  Description:  Cette classe dérivée de JFrame est la fenêtre 
 *  principale de l'application. Elle est composée d'un menu 
 *  (cf. méthode initMenu) et d'une zone de dessin implémentée 
 *  par la classe "ZoneDessin".
 * 
 *  History
 * 
 *  modified   by  rev reason
 *  ---------- --- --- ------
 *  2009-06-05 DMA     adapted to Eclipse and cleaned according to my taste
 *  
 * ###################################################################
 */

package fr.supelec.di.gl.td3;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class ApplicationDessin extends JFrame {

	private ZoneDessin zoneDessin;

	public ApplicationDessin(String title) {
		super(title);

		getContentPane().setLayout(new BorderLayout());

		initZoneDessin();
		initMenu();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	protected void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu menuDs = new JMenu("Dessiner");
		menuBar.add(menuDs);
		JMenu menuOp = new JMenu("Opérations");
		menuBar.add(menuOp);

		JMenuItem item;
		item = new JMenuItem("Rectangle");
		item.addActionListener(this.zoneDessin);
		menuDs.add(item);
		item = new JMenuItem("Losange");
		item.addActionListener(this.zoneDessin);
		menuDs.add(item);
		menuDs.addSeparator();
		item = new JMenuItem("Quitter");
		item.addActionListener(this.zoneDessin);
		menuDs.add(item);

		item = new JMenuItem("Copier");
		item.addActionListener(this.zoneDessin);
		menuOp.add(item);
		item = new JMenuItem("Coller");
		item.addActionListener(this.zoneDessin);
		menuOp.add(item);
		menuOp.addSeparator();
		item = new JMenuItem("Tout sélectionner");
		item.addActionListener(this.zoneDessin);
		menuOp.add(item);
		item = new JMenuItem("Tout désélectionner");
		item.addActionListener(this.zoneDessin);
		menuOp.add(item);
		menuOp.addSeparator();
		item = new JMenuItem("Grouper");
		item.addActionListener(this.zoneDessin);
		menuOp.add(item);
		item = new JMenuItem("Dégrouper");
		item.addActionListener(this.zoneDessin);
		menuOp.add(item);
		menuOp.addSeparator();
		item = new JMenuItem("Déplacer");
		item.addActionListener(this.zoneDessin);
		menuOp.add(item);
	}

	protected void initZoneDessin() {
		this.zoneDessin = new ZoneDessin();
		getContentPane().add(this.zoneDessin, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		ApplicationDessin app = new ApplicationDessin("Dessin");
		app.pack();
		app.setVisible(true);
	}

}

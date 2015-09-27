/* -*-Java-*-
 * ###################################################################
 * 
 *  FILE: "ApplicationDessin.java"
 *                                    created: 2002-10-11 
 *                
 *  Author: Bich-Li�n Doan
 *  E-mail: doan@supelec.fr
 *  
 *  Description:  Cette classe d�riv�e de JFrame est la fen�tre 
 *  principale de l'application. Elle est compos�e d'un menu 
 *  (cf. m�thode initMenu) et d'une zone de dessin impl�ment�e 
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
		JMenu menuOp = new JMenu("Op�rations");
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
		item = new JMenuItem("Tout s�lectionner");
		item.addActionListener(this.zoneDessin);
		menuOp.add(item);
		item = new JMenuItem("Tout d�s�lectionner");
		item.addActionListener(this.zoneDessin);
		menuOp.add(item);
		menuOp.addSeparator();
		item = new JMenuItem("Grouper");
		item.addActionListener(this.zoneDessin);
		menuOp.add(item);
		item = new JMenuItem("D�grouper");
		item.addActionListener(this.zoneDessin);
		menuOp.add(item);
		menuOp.addSeparator();
		item = new JMenuItem("D�placer");
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

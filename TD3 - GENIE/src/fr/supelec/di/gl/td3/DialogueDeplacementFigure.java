/* -*-Java-*-
 * ###################################################################
 * 
 *  FILE: "DeplFigDialog.java"
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

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class DialogueDeplacementFigure extends JDialog {

	private JTextField tX;
	private JTextField tY;

	private int cx, cy;

	private boolean ok;

	private JButton bOk;
	private JButton bCancel;

	public boolean okButton() {
		return this.ok;
	}

	public int getX() {
		return this.cx;
	}

	public int getY() {
		return this.cy;
	}

	public DialogueDeplacementFigure(Frame parent, String titre, int cx, int cy) {
		super(parent, titre, true);
		this.ok = false;

		this.cx = cx;
		this.cy = cy;

		Container panel = getContentPane();
		GridBagLayout grid = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();

		panel.setLayout(grid);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 4;
		constraints.gridheight = 1;
		JLabel label = new JLabel("Nouvelle postion de la figure");
		grid.setConstraints(label, constraints);
		panel.add(label);

		// position en X
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		label = new JLabel("Position X :");
		grid.setConstraints(label, constraints);
		panel.add(label);

		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		this.tX = new JTextField(String.valueOf(cx), 10);
		grid.setConstraints(this.tX, constraints);
		panel.add(this.tX);

		// position en Y
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		label = new JLabel("Position Y :");
		grid.setConstraints(label, constraints);
		panel.add(label);

		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		this.tY = new JTextField(String.valueOf(cy), 10);
		grid.setConstraints(this.tY, constraints);
		panel.add(this.tY);

		// bouton ok
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		this.bOk = new JButton("Ok");
		grid.setConstraints(this.bOk, constraints);
		panel.add(this.bOk);

		// bouton ok
		constraints.gridx = 2;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		this.bCancel = new JButton("Annuler");
		grid.setConstraints(this.bCancel, constraints);
		panel.add(this.bCancel);

		this.bOk.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (checkValues()) {
					DialogueDeplacementFigure.this.ok = true;
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null,
							"Certaines valeurs sont incorrectes");
				}
			}

		});

		this.bCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				DialogueDeplacementFigure.this.ok = false;
				setVisible(false);
			}

		});

	}

	protected boolean checkValues() {
		try {
			this.cx = Integer.parseInt(this.tX.getText());
			this.cy = Integer.parseInt(this.tY.getText());

			if ((this.cx < 0) || (this.cy < 0)) {
				return false;
			}

			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}

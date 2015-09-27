import java.awt.*;
import javax.swing.*;


public class JTableau extends JPanel {
	
	private int rows, cols;
	private JCampo[][] campos;
	private char[][] matrice ;
	
	
	public JTableau (char[][] matrice) {
		
		super();
		
		this.matrice = matrice;
		this.rows = matrice.length;
		this.cols = matrice[0].length;
		
		GridLayout grelha = new GridLayout (rows, cols);
		this.setLayout(grelha);
		
		campos = new JCampo[9][9];
		
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				
				
				/*if (matrice[i][j] == '#') */
					campos [i][j] = new JCampo(JCampo.PAS_EN_USE);
				/*else if (matrice[i][j] == '1')
					campos [i][j] = new JCampo(JCampo.EN_USE);
				else 
					campos [i][j] = new JCampo(JCampo.VIDE);*/
				
				this.add(campos[i][j]);
			}
		
		//campos[0][3].type = JCampo.VIDE;
	}
	
	public void paintComponent (Graphics g) {
		
		super.paintComponent(g);
		
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				campos[i][j].paint(g);
			}
	}

}

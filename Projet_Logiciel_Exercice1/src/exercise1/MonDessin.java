package exercise1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MonDessin extends JPanel {
	
	private Image img;
	
	public void paint (Graphics g) {
		
		g.setColor(Color.RED);
		g.fillRect(100, 100, 100, 100);
		
		
		File f = new File("/home/gciotto/workspace/exercice1/src/teste.png");
		
		try {
			img = ImageIO.read(f);
			g.drawImage(img, 250, 250, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		
	}

}

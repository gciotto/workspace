import java.awt.*;
import javax.swing.JPanel;


public class JCampo extends JPanel {

	int type;
	
	public static int VIDE = 0, EN_USE = 1, PAS_EN_USE = 2;
	
	public JCampo () {
		super();
		this.type = JCampo.PAS_EN_USE;
	}
	
	public JCampo (int type) {
		super();
		this.type = type;
	}
	
	
	
	public void paint(Graphics g) {
		
		Dimension d = this.getSize();
		
		Color c = Color.LIGHT_GRAY;
		
		switch (this.type) {
		
			case 1 : c = Color.BLACK; break;
			case 0 : c = Color.WHITE; break;
			case 2 : c = Color.LIGHT_GRAY; break;
		
		}
		
		g.setColor(c);
		
		g.fillOval(0, 0,((int) (0.85*d.getWidth())),((int) (0.85*d.getWidth())));
		
	}
}

package br.cnpem.gef.figure;

import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class NetworkInterfaceFigure extends Figure {
	
	public static final int ETHERNET = 0;
	
	private static String ETHERNET_ICON = "/home/gciotto/eclipse-CSS4_4-LUNA/workspace/br.cnpem.gef.golpe/icon/ethernet.png";
	private ImageFigure icon;
	private Label name, address;
	
	public NetworkInterfaceFigure (){
		
		GridLayout layout = new GridLayout(3, false);
		
		icon = new ImageFigure ();
		name = new Label();
		address = new Label();	
		
		this.setLayoutManager(layout);
		this.add(icon);
		this.add(name);
		this.add(address);
	
	}
	
	public void setName(String interfaceName) {
		this.name.setText(interfaceName);
	}
	
	public void setAddress(String interfaceAddress) {
		this.address.setText(interfaceAddress);
	}

	public void setIcon(int iconType) {
		switch (iconType) {
			case ETHERNET: 
				this.icon.setImage(new Image(Display.getCurrent(), ETHERNET_ICON));
				break;
		}
	}
}

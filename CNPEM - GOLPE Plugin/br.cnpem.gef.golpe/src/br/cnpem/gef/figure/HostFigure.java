package br.cnpem.gef.figure;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;

public class HostFigure extends ComponentFigure {
	
	protected List<NetworkInterfaceFigure> network_figures;
	
	
	public HostFigure() {
		super();
		this.network_figures = new ArrayList<NetworkInterfaceFigure>();
	}
	
	public void addNetworkInterfaceFigure (NetworkInterfaceFigure m) {
		this.network_figures.add(m);
	}
	
	public void removeNetworkInterfaceFigure (NetworkInterfaceFigure m) {
		this.network_figures.remove(m);
	}
		
}

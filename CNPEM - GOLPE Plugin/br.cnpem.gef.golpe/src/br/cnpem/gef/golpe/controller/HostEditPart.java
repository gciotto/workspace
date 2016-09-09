package br.cnpem.gef.golpe.controller;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import br.cnpem.gef.figure.ComponentFigure;
import br.cnpem.gef.figure.HostFigure;
import br.cnpem.gef.golpe.model.Component;
import br.cnpem.gef.golpe.model.Host;
import br.cnpem.gef.golpe.model.NetworkInterface;
import br.cnpem.gef.golpe.policy.HostXYLayoutEditPolicy;
import br.cnpem.gef.golpe.policy.RootXYLayoutPolicy;

public class HostEditPart extends GenericComponentEditPart {
	
	private HostFigure _figure;
	
	public HostFigure getHostFigure (){
		return _figure;
	}
	
	@Override
	public IFigure getContentPane() {
		return ((HostFigure) getFigure()).interface_container;
	}
	
	@Override
	protected IFigure createFigure() {
		_figure = new HostFigure();
		return _figure;
	}
	
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new HostXYLayoutEditPolicy());
	}
	
	@Override
	public List getModelChildren() {
		return ((Host) getModel()).getNetworkInterfaces();
	}
	
}



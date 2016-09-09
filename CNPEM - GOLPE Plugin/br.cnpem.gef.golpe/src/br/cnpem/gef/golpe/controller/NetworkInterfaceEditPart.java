package br.cnpem.gef.golpe.controller;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.omg.CORBA._PolicyStub;

import br.cnpem.gef.figure.NetworkInterfaceFigure;
import br.cnpem.gef.golpe.model.Component;
import br.cnpem.gef.golpe.model.Host;
import br.cnpem.gef.golpe.model.NetworkInterface;
import br.cnpem.gef.golpe.policy.GenericComponentEditPolicy;
import br.cnpem.gef.golpe.policy.NetworkInterfaceDeleteEditPolicy;

public class NetworkInterfaceEditPart extends AbstractGraphicalEditPart implements IAdaptable {

	NetworkInterfaceObserver _observer;
	
	public NetworkInterfaceEditPart() {
		super();
		_observer = new NetworkInterfaceObserver();
	}
	
	@Override
	protected IFigure createFigure() {
		IFigure f = new NetworkInterfaceFigure();
		//((HostEditPart) getParent()).getHostFigure().addNetworkInterfaceFigure((NetworkInterfaceFigure) f);
		return f;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new NetworkInterfaceDeleteEditPolicy());
	}
	
	@Override
	protected void refreshVisuals() {
		
		NetworkInterfaceFigure _figure = (NetworkInterfaceFigure) getFigure();
		
		NetworkInterface _model = (NetworkInterface) getModel();
		
		_figure.setAddress(_model.getIp_address());
		_figure.setName(_model.getName());
		_figure.setIcon(NetworkInterfaceFigure.ETHERNET);
		
		HostEditPart parent = (HostEditPart) getParent();
		
		int i = ((Host)parent.getModel()).getNetworkInterfaces().indexOf(this.getModel());
		
				
		parent.setLayoutConstraint(this, _figure, new Rectangle(new Point(5 , i * 35), new Dimension(180,35)));
		
	}
	
	@Override
	public void activate() {

		if (!isActive())
			((NetworkInterface)getModel()).addObserver(_observer);

		super.activate();
	}

	@Override
	public void deactivate() {

		if (isActive())
			((NetworkInterface)getModel()).deleteObserver(_observer);

		super.deactivate();
	}
	
	public class NetworkInterfaceObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			refreshVisuals();
		}
		
	}
	
	@Override
	public Object getAdapter(Class key) {
		if (key.equals(IPropertySource.class)) {
			return getModel();
		}
		return super.getAdapter(key);
	}


}

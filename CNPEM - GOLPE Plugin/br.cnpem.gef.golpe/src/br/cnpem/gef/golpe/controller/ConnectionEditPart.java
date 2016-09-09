package br.cnpem.gef.golpe.controller;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.ui.views.properties.IPropertySource;

import br.cnpem.gef.golpe.policy.ConnectionModelEditPolicy;

public class ConnectionEditPart extends AbstractConnectionEditPart {

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionModelEditPolicy());
	}
	
	@Override
	protected IFigure createFigure() {
		PolylineConnection conn = new PolylineConnection();
		
		conn.setLineStyle(SWT.LINE_DOT);
		
		return conn;
	}
	
	@Override
	public Object getAdapter(Class key) {
		if (key.equals(IPropertySource.class)) {
			return getModel();
		}
		return super.getAdapter(key);
	}

}

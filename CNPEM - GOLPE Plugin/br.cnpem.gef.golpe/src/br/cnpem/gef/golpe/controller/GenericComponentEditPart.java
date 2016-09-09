package br.cnpem.gef.golpe.controller;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.IPropertySource;

import br.cnpem.gef.figure.ComponentFigure;
import br.cnpem.gef.golpe.model.Component;
import br.cnpem.gef.golpe.model.Connection;
import br.cnpem.gef.golpe.model.RootComponent;
import br.cnpem.gef.golpe.policy.ConnectionGraphicalNodeEditPolicy;
import br.cnpem.gef.golpe.policy.GenericComponentEditPolicy;

public class GenericComponentEditPart extends AbstractGraphicalEditPart implements NodeEditPart, IAdaptable {

	protected GenericComponentObserver _observer;

	public GenericComponentEditPart() {
		super();
		_observer = new GenericComponentObserver();
	}

	@Override
	protected IFigure createFigure() {
		return new ComponentFigure();
	}


	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new GenericComponentEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ConnectionGraphicalNodeEditPolicy());
	}
	
	@Override
	protected void refreshVisuals() {

		ComponentFigure _figure = (ComponentFigure) getFigure();

		Component _model = (Component) getModel();

		_figure.getImageIcon().setImage(new Image(Display.getCurrent(), _model.getIconRoot()));

		_figure.setComponentName(_model.getName());

		RootEditPart parent = (RootEditPart) getParent();

		parent.setLayoutConstraint(this, _figure, new Rectangle(_model.getScreenLocation().x, _model.getScreenLocation().y,
				_model.getDimensions().width, _model.getDimensions().height));
	}

	@Override
	public void activate() {

		if (!isActive())
			((Component)getModel()).addObserver(_observer);

		super.activate();
	}

	@Override
	public void deactivate() {

		if (isActive())
			((Component)getModel()).deleteObserver(_observer);

		super.deactivate();
	}

	@Override
	protected List<Connection> getModelSourceConnections() {
		return ((RootComponent)getParent().getModel()).getConnectionsWithAt((Component) getModel(), Connection.EXTREMITY1);
	}
	
	@Override
	protected List<Connection> getModelTargetConnections() {
		return ((RootComponent)getParent().getModel()).getConnectionsWithAt((Component) getModel(), Connection.EXTREMITY2);
	}
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart arg0) {
		return ((ComponentFigure)getFigure()).getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request arg0) {
		return ((ComponentFigure)getFigure()).getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart arg0) {
		return ((ComponentFigure)getFigure()).getConnectionAnchor();
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request arg0) {
		return ((ComponentFigure)getFigure()).getConnectionAnchor();
	}

	@Override
	protected void refreshChildren() {
		List a = getChildren();
		super.refreshChildren();
	}
	
	public class GenericComponentObserver implements Observer {

		@Override
		public void update(Observable arg0, Object arg1) {
			refreshVisuals();
			refreshSourceConnections();
			refreshTargetConnections();
			refreshChildren();
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

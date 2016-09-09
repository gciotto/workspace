package br.cnpem.gef.golpe.controller;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import br.cnpem.gef.golpe.model.RootComponent;
import br.cnpem.gef.golpe.policy.RootXYLayoutPolicy;

public class RootEditPart extends AbstractGraphicalEditPart {
		
	private RootModelObserver _observer; 
	
	public RootEditPart() {
		super();
		_observer = new RootModelObserver();		
	}
	
	@Override
	protected IFigure createFigure() {
		Figure f = new FreeformLayer();
		
		f.setBackgroundColor(ColorConstants.lightGray);
		f.setLayoutManager(new FreeformLayout());
		f.setOpaque(true);
		
		return f;
	}

	@Override
	protected void createEditPolicies() {
		System.out.println("[GolpeRootEditPart] createEditPolicies");
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new RootXYLayoutPolicy());
	}
	
	@Override
	protected List getModelChildren() {
				
		return ((RootComponent) getModel()).getComponentList();
	}

	@Override
	public void activate() {
		if (!isActive()) 
			((RootComponent) getModel()).addObserver(_observer);
		
		super.activate();
	}
	
	@Override
	public void deactivate() {
		
		if (isActive())
			((RootComponent) getModel()).addObserver(_observer);
		
		super.deactivate();
	}
	
	public class RootModelObserver implements Observer {

		@Override
		public void update(Observable arg0, Object arg1) {
			refreshChildren();
		}
	}
	
}

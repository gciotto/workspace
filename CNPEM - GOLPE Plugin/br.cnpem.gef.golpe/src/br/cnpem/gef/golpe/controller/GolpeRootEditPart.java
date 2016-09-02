package br.cnpem.gef.golpe.controller;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import br.cnpem.gef.golpe.policy.EGolpeRootXYLayoutPolicy;
import emodel.EGolpeComponentModel;
import emodel.EGolpeModel;

public class GolpeRootEditPart extends AbstractGraphicalEditPart {
		
	private GolpeRootModelObserver _observer; 
	
	public GolpeRootEditPart() {
		super();
		_observer = new GolpeRootModelObserver();		
	}
	
	@Override
	protected IFigure createFigure() {
		Figure f = new FreeformLayer();
		
		f.setBackgroundColor(ColorConstants.lightGray);
		f.setLayoutManager(new FreeformLayout());
		f.setOpaque(true);
		f.setBorder(new LineBorder(1));
		
		return f;
	}

	@Override
	protected void createEditPolicies() {
		System.out.println("[GolpeRootEditPart] createEditPolicies");
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new EGolpeRootXYLayoutPolicy());
	}
	
	@Override
	protected List<EGolpeComponentModel> getModelChildren() {
				
		return ((EGolpeModel) getModel()).getComponents();
	}

	@Override
	public void activate() {
		if (!isActive()) 
			((EGolpeModel) getModel()).eAdapters().add(_observer);
		
		super.activate();
	}
	
	@Override
	public void deactivate() {
		
		if (isActive())
			((EGolpeModel) getModel()).eAdapters().add(_observer);
		
		super.deactivate();
	}
	
	public class GolpeRootModelObserver implements Adapter {

		@Override
		public void notifyChanged(Notification notification) {
			refreshChildren();
			
		}

		@Override
		public Notifier getTarget() {
			return (EGolpeModel) getModel();
		}

		@Override
		public void setTarget(Notifier newTarget) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isAdapterForType(Object type) {
			return type.equals(EGolpeModel.class);
		}
		
	}
	
}

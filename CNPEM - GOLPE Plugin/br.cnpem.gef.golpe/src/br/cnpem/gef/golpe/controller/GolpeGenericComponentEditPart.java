package br.cnpem.gef.golpe.controller;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import emodel.EGolpeComponentModel;
import br.cnpem.gef.figure.GolpeComponentFigure;

public class GolpeGenericComponentEditPart extends AbstractGraphicalEditPart {
	
	@Override
	protected IFigure createFigure() {
		return new GolpeComponentFigure();
	}

	
	@Override
	protected void createEditPolicies() {
		System.out.println("Called createEditPolicies()");
	}

	@Override
	protected void refreshVisuals() {

		System.out.println("entrou");
		
		GolpeComponentFigure _figure = (GolpeComponentFigure) getFigure();
		
		EGolpeComponentModel _model = (EGolpeComponentModel) getModel();
		
		_figure.getImageIcon().setImage(new Image(Display.getCurrent(), _model.getIconAddress()));
		
		_figure.getLabel().setText(_model.getName());
		
		GolpeRootEditPart parent = (GolpeRootEditPart) getParent();
		
		Rectangle _constraints = _model.getConstraint();
		
		parent.setLayoutConstraint(this, _figure, new Rectangle(_constraints.x, _constraints.y,
									_constraints.width, _constraints.height));
	}

	
}

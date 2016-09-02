package br.cnpem.gef.golpe.controller;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import emodel.EGolpeComponentModel;
import emodel.EGolpeHostModel;
import emodel.EGolpeModel;


public class GolpeEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart iContext, Object iModel) {
		System.out.println("Called GraphicalPartFactory.createEditPart("
				+ iContext + "," + iContext + ")");
 
		EditPart editPart = null;
		if (iModel instanceof EGolpeModel)	
			editPart = new GolpeRootEditPart();
		
		else if (iModel instanceof EGolpeComponentModel || iModel instanceof EGolpeHostModel) 
			editPart = new GolpeGenericComponentEditPart();
 
		if (editPart != null)
			editPart.setModel(iModel);
		
		
		return editPart;
	}

}

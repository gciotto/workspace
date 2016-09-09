package br.cnpem.gef.golpe.controller;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import br.cnpem.gef.golpe.model.Component;
import br.cnpem.gef.golpe.model.Connection;
import br.cnpem.gef.golpe.model.Host;
import br.cnpem.gef.golpe.model.NetworkInterface;
import br.cnpem.gef.golpe.model.RootComponent;


public class ComponentEditPartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart iContext, Object iModel) {
		System.out.println("Called GraphicalPartFactory.createEditPart("
				+ iContext + "," + iModel + ")");
 
		EditPart editPart = null;
		if (iModel instanceof RootComponent)	
			editPart = new RootEditPart();
		
		else if (iModel instanceof Host)
			editPart = new HostEditPart();
		
		else if (iModel instanceof NetworkInterface)
			editPart = new NetworkInterfaceEditPart();
		
		else if (iModel instanceof Component) 
			editPart = new GenericComponentEditPart();
 
		else if (iModel instanceof Connection)
			editPart = new ConnectionEditPart();
		
		if (editPart != null)
			editPart.setModel(iModel);
		
		
		return editPart;
	}

}

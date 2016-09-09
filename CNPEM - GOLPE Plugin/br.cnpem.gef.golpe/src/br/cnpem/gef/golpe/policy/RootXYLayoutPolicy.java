package br.cnpem.gef.golpe.policy;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import br.cnpem.gef.command.GenericComponentModelCreateCommand;
import br.cnpem.gef.command.GenericComponentResizeCommand;
import br.cnpem.gef.golpe.model.BBB;
import br.cnpem.gef.golpe.model.BBG;
import br.cnpem.gef.golpe.model.Component;
import br.cnpem.gef.golpe.model.RootComponent;

public class RootXYLayoutPolicy extends XYLayoutEditPolicy {
	
	@Override
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		
		GenericComponentResizeCommand _c = new GenericComponentResizeCommand();
		
		_c.setModel((Component) child.getModel());
		
		Rectangle r = (Rectangle) constraint;
		
		_c.setNewDimension(new Dimension (r.width, r.height));
		_c.setNewScreenLocation(r.getLocation());
		
		return _c;
	}
	
	@Override
	protected Command getCreateCommand(CreateRequest _request) {
		
		Command _command = null;
		
		if (_request.getNewObject() instanceof Component) {
			
			GenericComponentModelCreateCommand _c = new GenericComponentModelCreateCommand();
			_c.setComponent((Component) _request.getNewObject());
			_c.setRootModel((RootComponent) getHost().getModel());
			_c.setScreenLocation(_request.getLocation());

			_command = _c;
		}
		
		return _command;
	}

}

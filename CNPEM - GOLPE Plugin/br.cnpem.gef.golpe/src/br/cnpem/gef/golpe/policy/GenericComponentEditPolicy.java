package br.cnpem.gef.golpe.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import br.cnpem.gef.command.GenericComponentDeleteCommand;
import br.cnpem.gef.golpe.model.Component;
import br.cnpem.gef.golpe.model.RootComponent;

public class GenericComponentEditPolicy extends ComponentEditPolicy {
	
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		
		GenericComponentDeleteCommand _c = new GenericComponentDeleteCommand();
		
		_c.setModel((Component) getHost().getModel());
		_c.setRootModel((RootComponent) getHost().getParent().getModel());
		
		return _c;
	}

}

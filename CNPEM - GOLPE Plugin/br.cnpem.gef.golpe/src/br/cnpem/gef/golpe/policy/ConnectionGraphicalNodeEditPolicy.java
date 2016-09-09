package br.cnpem.gef.golpe.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import br.cnpem.gef.command.ConnectionCreateCommand;
import br.cnpem.gef.golpe.model.Component;
import br.cnpem.gef.golpe.model.Connection;
import br.cnpem.gef.golpe.model.RootComponent;

public class ConnectionGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest arg0) {
		ConnectionCreateCommand _c = (ConnectionCreateCommand) arg0.getStartCommand();
		_c.setTarget((Component) getHost().getModel());
		return _c;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest arg0) {
		ConnectionCreateCommand _c = new ConnectionCreateCommand();
		_c.setSource((Component) getHost().getModel());
		_c.setConnection((Connection) arg0.getNewObject());
		_c.setRootModel((RootComponent) getHost().getParent().getModel());
		
		arg0.setStartCommand(_c);
		return _c;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

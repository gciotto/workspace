package br.cnpem.gef.golpe.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import br.cnpem.gef.command.NetworkInterfaceCreateCommand;
import br.cnpem.gef.golpe.model.Host;
import br.cnpem.gef.golpe.model.NetworkInterface;

public class HostXYLayoutEditPolicy extends XYLayoutEditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest _request) {
		
		Command _command = null;
		
		if (_request.getNewObject() instanceof NetworkInterface) {
			
			NetworkInterfaceCreateCommand _c = new NetworkInterfaceCreateCommand();
			_c.setNetworkInterface( (NetworkInterface) _request.getNewObject());
			_c.setHostRoot((Host) getHost().getModel());
			
			_command = _c;
		}
		
		return _command;
	}

}

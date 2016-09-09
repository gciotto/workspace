package br.cnpem.gef.command;

import org.eclipse.gef.commands.Command;

import br.cnpem.gef.golpe.model.Component;
import br.cnpem.gef.golpe.model.Connection;
import br.cnpem.gef.golpe.model.Host;
import br.cnpem.gef.golpe.model.RootComponent;
import br.cnpem.gef.golpe.model.Switch;

public class ConnectionCreateCommand extends Command {

	private Component source, target;
	private Connection connection;

	private RootComponent _root;

	@Override
	public boolean canExecute() {

		boolean extraConstraints = true;

		if (source instanceof Host || target instanceof Host ) {

			if (source instanceof Host)
				extraConstraints &= (_root.getConnectionsWith(source).size() < ((Host) source).getMaxNInterface()); 
			else
				extraConstraints &= (_root.getConnectionsWith(target).size() < ((Host) target).getMaxNInterface());
		}

		if (source instanceof Switch || target instanceof Switch ) {

			if (source instanceof Switch)
				extraConstraints &= (_root.getConnectionsWith(source).size() < ((Switch) source).getNumberPorts()); 
			else
				extraConstraints &= (_root.getConnectionsWith(target).size() < ((Switch) target).getNumberPorts());
		}


		return ((source instanceof Switch || target instanceof Switch) && extraConstraints);
	}

	@Override
	public void execute() {
		connection.setExtremity1(source);
		connection.setExtremity2(target);
		connection.setRoot(_root);
		_root.addConnection(connection);
	}

	@Override
	public void undo() {
		_root.removeConnection(connection);
	}

	public void setSource(Component source) {
		this.source = source;
	}

	public void setTarget(Component target) {
		this.target = target;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setRootModel(RootComponent _root) {
		this._root = _root;
	}

}

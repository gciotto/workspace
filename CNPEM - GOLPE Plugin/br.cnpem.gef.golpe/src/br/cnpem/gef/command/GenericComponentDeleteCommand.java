package br.cnpem.gef.command;

import java.util.List;

import org.eclipse.gef.commands.Command;

import br.cnpem.gef.golpe.model.Component;
import br.cnpem.gef.golpe.model.Connection;
import br.cnpem.gef.golpe.model.RootComponent;

public class GenericComponentDeleteCommand extends Command {
	
	private Component _model;
	private RootComponent _root;
	private List<Connection> _connections;
	
	@Override
	public void execute() {
		
		_connections = _root.getConnectionsWith(_model);
		
		for (Connection c : _connections) 
			_root.removeConnection(c);
		
		_root.removeComponent(_model);
		
	}
	
	@Override
	public void undo() {
		for (Connection c : _connections) 
			_root.addConnection(c);
		_root.addComponent(_model);
	}
	
	public void setModel(Component _m) {
		this._model = _m;
	}
	
	public void setRootModel(RootComponent _r) {
		this._root = _r;
	}

}

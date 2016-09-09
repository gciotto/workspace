package br.cnpem.gef.command;

import org.eclipse.gef.commands.Command;

import br.cnpem.gef.golpe.model.Component;

public class GenericComponentModelRenameCommand extends Command {
	
	private String _old, _new;
	private Component _model;
	
	@Override
	public void execute() {
		_old = _model.getName();
		_model.setName(_new);
	}
	
	@Override
	public void undo() {
		_model.setName(_old);
	}
	
	public void setModel(Component m) {
		this._model = m;
	}
	
	public void setNewName(String _n) {
		this._new = _n;
	}
	

}

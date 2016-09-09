package br.cnpem.gef.command;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import br.cnpem.gef.golpe.model.Component;
import br.cnpem.gef.golpe.model.RootComponent;

public class GenericComponentModelCreateCommand extends Command {
	
	private Component _new_component;
	private RootComponent _root;

	@Override
	public void execute() {
		_root.addComponent(_new_component);
	}
		
	@Override
	public void undo() {
		_root.removeComponent(_new_component);
	}

	public void setComponent(Component _new_component) {
		this._new_component = _new_component;
	}
	
	public void setRootModel (RootComponent _r) {
		this._root = _r;
	}
	
	public void setScreenLocation (Point point) {
		this._new_component.setScreenLocation(point);
	}


}

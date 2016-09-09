package br.cnpem.gef.command;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import br.cnpem.gef.golpe.model.Component;

public class GenericComponentResizeCommand extends Command {
	
	private Component _model;
	private Dimension _old, _new;
	private Point _old_p, _new_p;
	
	@Override
	public void execute() {
		
		_old = this._model.getDimensions();
		_old_p = this._model.getScreenLocation();
		
		this._model.setDimensions(_new);
		this._model.setScreenLocation(_new_p);
		super.execute();
	}
	
	@Override
	public void undo() {
		_model.setDimensions(_old);	
		_model.setScreenLocation(_old_p);
	}
	
	public void setModel(Component m) {
		this._model = m;
	}
	
	public void setNewDimension (Dimension n) {
		this._new = n;
	}
	
	public void setNewScreenLocation(Point n) {
		this._new_p = n;	
	}

}

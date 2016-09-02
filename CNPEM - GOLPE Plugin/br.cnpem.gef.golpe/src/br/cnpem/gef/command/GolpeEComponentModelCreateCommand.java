package br.cnpem.gef.command;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import emodel.EGolpeComponentModel;
import emodel.EGolpeModel;

public class GolpeEComponentModelCreateCommand extends Command {
	
	private static final Dimension defaultDimension = new Dimension(150, 150);
	private static final String defaultName = "<...>";
	private static final String defaultPath = "/home/gciotto/eclipse-CSS4_4-LUNA/workspace/br.cnpem.gef.golpe/LNLS_logo256.gif";
	
	private EGolpeComponentModel _new_component;
	private Rectangle _constraint;
	private EGolpeModel _root;
	
	@Override
	public void execute() {
		_new_component.setName(defaultName);
		_new_component.setConstraint(_constraint);
		_new_component.setRootModel(_root);
		_new_component.setIconAddress(defaultPath);
	}
	
	public void setIconPath(String path) {
		
		_new_component.setIconAddress(path);
	}
	
	@Override
	public void undo() {
		_root.getComponents().remove(_new_component);
	}

	public void setEComponent(EGolpeComponentModel _new_component) {
		this._new_component = _new_component;
	}

	public void setEConstraint(Point point) {
		this._constraint = new Rectangle (point, defaultDimension);
	}

	public void setRootModel (EGolpeModel _root) {
		this._root = _root;
	}

}

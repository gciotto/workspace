package br.cnpem.gef.golpe.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import br.cnpem.gef.command.GenericComponentModelRenameCommand;
import br.cnpem.gef.figure.ComponentFigure;
import br.cnpem.gef.golpe.model.Component;

public class ComponentEditorEditPolicy extends DirectEditPolicy {

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		GenericComponentModelRenameCommand _c = new GenericComponentModelRenameCommand();
		
		_c.setModel((Component) getHost().getModel());
		_c.setNewName((String) request.getCellEditor().getValue());
		
		return _c;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String) request.getCellEditor().getValue();
		((ComponentFigure)getHostFigure()).setComponentName(value);
	}

}

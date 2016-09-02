package br.cnpem.gef.golpe.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import br.cnpem.gef.command.GolpeEComponentModelCreateCommand;
import br.cnpem.gef.command.GolpeEHostModelCreateCommand;
import emodel.EGolpeComponentModel;
import emodel.EGolpeHostModel;
import emodel.EGolpeModel;

public class EGolpeRootXYLayoutPolicy extends XYLayoutEditPolicy {

	@Override
	protected Command getCreateCommand(CreateRequest _request) {
		
		Command _command = null;
		
		if (_request.getNewObject() instanceof EGolpeHostModel) {
			
			GolpeEHostModelCreateCommand _c = new GolpeEHostModelCreateCommand();
			_c.setEConstraint(_request.getLocation());
			_c.setEComponent((EGolpeComponentModel) _request.getNewObject());
			_c.setRootModel((EGolpeModel) getHost().getModel());
			_c.setIconPath("/home/gciotto/eclipse-CSS4_4-LUNA/workspace/br.cnpem.gef.golpe/icon/bbb.png");
			
			_command = _c;
		}
		
		return _command;
	}

}

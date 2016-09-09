package br.cnpem.gef.golpe.model;

import org.eclipse.gef.requests.CreationFactory;

public class SwitchFactory implements CreationFactory {

	@Override
	public Object getNewObject() {
		return new Switch(null, "Generic Switch");
	}

	@Override
	public Object getObjectType() {
		return Switch.class;
	}

}

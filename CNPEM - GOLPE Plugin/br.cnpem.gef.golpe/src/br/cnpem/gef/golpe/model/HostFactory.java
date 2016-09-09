package br.cnpem.gef.golpe.model;

import org.eclipse.gef.requests.CreationFactory;

public class HostFactory implements CreationFactory {

	@Override
	public Object getNewObject() {
		return new Host(null, "Generic host");
	}

	@Override
	public Object getObjectType() {
		return Host.class;
	}

}

package br.cnpem.gef.golpe.model;

import org.eclipse.gef.requests.CreationFactory;

public class BBBFactory implements CreationFactory {

	@Override
	public Object getNewObject() {
		return new BBB(null, "New BBB board");
	}

	@Override
	public Object getObjectType() {
		return BBB.class;
	}

}

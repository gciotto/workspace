package br.cnpem.gef.golpe.model;

import org.eclipse.gef.requests.CreationFactory;

public class BBGFactory implements CreationFactory {

	@Override
	public Object getNewObject() {
		return new BBG(null, "New BBG board");
	}

	@Override
	public Object getObjectType() {
		return BBG.class;
	}

}

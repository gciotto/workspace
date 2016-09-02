package br.cnpem.gef.factory;

import org.eclipse.gef.requests.CreationFactory;

import emodel.EGolpeModel;
import emodel.EmodelFactory;

public class EGolpeModelFactory implements CreationFactory {

	@Override
	public Object getNewObject() {
		return EmodelFactory.eINSTANCE.createEGolpeModel();
	}

	@Override
	public Object getObjectType() {
		return EGolpeModel.class;
	}

}

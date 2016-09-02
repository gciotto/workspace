package br.cnpem.gef.factory;

import org.eclipse.gef.requests.CreationFactory;

import emodel.EGolpeComponentModel;
import emodel.EGolpeHostModel;
import emodel.EmodelFactory;

public class EGolpeHostFactory implements CreationFactory {

	@Override
	public Object getNewObject() {
		return EmodelFactory.eINSTANCE.createEGolpeHostModel();
	}

	@Override
	public Object getObjectType() {
		return EGolpeComponentModel.class;
	}

}

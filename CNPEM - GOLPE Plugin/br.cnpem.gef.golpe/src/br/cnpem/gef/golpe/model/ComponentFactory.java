package br.cnpem.gef.golpe.model;

import org.eclipse.gef.requests.CreationFactory;

public class ComponentFactory implements CreationFactory  {
	
	public Component createGolpeComponent(Place location, String name) {
		return new Component(location, name);
	}

	@Override
	public Object getNewObject() {
		return createGolpeComponent(null, "Generic device");
	}

	@Override
	public Object getObjectType() {
		return Component.class;
	}

}

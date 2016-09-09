package br.cnpem.gef.golpe.model;

import org.eclipse.gef.requests.CreationFactory;

public class NetworkInterfaceFactory implements CreationFactory {

	@Override
	public Object getNewObject() {
		return new NetworkInterface("192.168.7.2","eth0", "RJ45", false);
	}

	@Override
	public Object getObjectType() {
		return NetworkInterface.class;
	}

}

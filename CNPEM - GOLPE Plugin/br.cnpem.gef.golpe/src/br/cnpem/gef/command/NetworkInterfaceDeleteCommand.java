package br.cnpem.gef.command;

import org.eclipse.gef.commands.Command;

import br.cnpem.gef.golpe.model.Host;
import br.cnpem.gef.golpe.model.NetworkInterface;

public class NetworkInterfaceDeleteCommand extends Command {
	
	private NetworkInterface _interface;
	private Host _root;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		_root.removeNetworkInterface(_interface);
	}
	
	@Override
	public void undo() {
		_root.addNetworkInterface(_interface);
	}
	
	
	public void setNetworkInterface(NetworkInterface i) {
		this._interface = i;
	}
	
	public void setHostRoot(Host h) {
		this._root = h;
	}

}

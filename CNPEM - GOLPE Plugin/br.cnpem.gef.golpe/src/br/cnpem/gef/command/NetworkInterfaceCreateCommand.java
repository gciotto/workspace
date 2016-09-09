package br.cnpem.gef.command;

import org.eclipse.gef.commands.Command;

import br.cnpem.gef.golpe.model.Host;
import br.cnpem.gef.golpe.model.NetworkInterface;

public class NetworkInterfaceCreateCommand extends Command {
	
	private NetworkInterface _interface;
	private Host _root;
	
	@Override
	public boolean canExecute() {
		return (_root.getNetworkInterfaces().size() < this._root.getMaxNInterface()); 
	}
	
	@Override
	public void execute() {
		_root.addNetworkInterface(_interface);
	}
	
	@Override
	public void undo() {
		_root.removeNetworkInterface(_interface);
	}
	
	public void setNetworkInterface(NetworkInterface i) {
		this._interface = i;
	}
	
	public void setHostRoot(Host h) {
		this._root = h;
	}
	
}

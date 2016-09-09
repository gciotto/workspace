package br.cnpem.gef.palette;

import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;

import br.cnpem.gef.golpe.model.BBBFactory;
import br.cnpem.gef.golpe.model.BBGFactory;
import br.cnpem.gef.golpe.model.ComponentFactory;
import br.cnpem.gef.golpe.model.ConnectionFactory;
import br.cnpem.gef.golpe.model.HostFactory;
import br.cnpem.gef.golpe.model.NetworkInterfaceFactory;
import br.cnpem.gef.golpe.model.SwitchFactory;

public class GolpeEditorPalette extends PaletteRoot {

	private PaletteGroup _group;

	public GolpeEditorPalette (){

		addGroup();
		
		_group.setVisible(true);
		
		
		addSelectionTool();
		addEGolpeHostTool();
		
		ConnectionCreationToolEntry entry = new ConnectionCreationToolEntry("Link", "Creates a new link", new ConnectionFactory(), null, null);
	    _group.add(entry);
	}

	private void addSelectionTool() {
		SelectionToolEntry entry = new SelectionToolEntry();
		_group.add(entry);
		setDefaultEntry(entry);
	}

	private void addGroup() {
		_group = new PaletteGroup("New hosts");
		add(_group);
		
	}

	private void addEGolpeHostTool() {
		CreationToolEntry 	entry_bbb = new CreationToolEntry("BeagleBone Black ", "Create a new BBB", new BBBFactory(), null, null),
							entry_bbg = new CreationToolEntry("BeagleBone Green ", "Create a new BBG", new BBGFactory(), null, null),
							entry_generic = new CreationToolEntry("Generic device", "Create a new device", new HostFactory(), null, null),
							entry_switch = new CreationToolEntry("Switch", "Create a new switch", new SwitchFactory(), null, null),
							entry_network_interface = new CreationToolEntry("Network Interface", "Create a new network interface", new NetworkInterfaceFactory(), null, null);
		_group.add(entry_bbb);
		_group.add(entry_bbg);
		_group.add(entry_generic);
		_group.add(entry_switch);
		_group.add(entry_network_interface);
		
	}


}

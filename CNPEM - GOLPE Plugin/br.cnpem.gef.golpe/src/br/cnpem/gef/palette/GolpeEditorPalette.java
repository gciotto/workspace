package br.cnpem.gef.palette;

import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;

import br.cnpem.gef.factory.EGolpeHostFactory;

public class GolpeEditorPalette extends PaletteRoot {

	private PaletteGroup _group;

	public GolpeEditorPalette (){

		addGroup();
		addSelectionTool();
		addEGolpeHostTool();
	}

	private void addSelectionTool() {
		SelectionToolEntry entry = new SelectionToolEntry();
		_group.add(entry);
		setDefaultEntry(entry);
	}

	private void addGroup() {
		_group = new PaletteGroup("Golpe Controls");
		add(_group);
	}

	private void addEGolpeHostTool() {
		CreationToolEntry entry = new CreationToolEntry("Host ", "Create a new Object", new EGolpeHostFactory(), null, null);
		_group.add(entry);
	}


}

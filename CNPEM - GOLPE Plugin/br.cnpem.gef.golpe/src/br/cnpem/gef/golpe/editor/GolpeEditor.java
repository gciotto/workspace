package br.cnpem.gef.golpe.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.properties.UndoablePropertySheetPage;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;

import br.cnpem.gef.golpe.controller.ComponentEditPartFactory;
import br.cnpem.gef.golpe.model.Component;
import br.cnpem.gef.golpe.model.RootComponent;
import br.cnpem.gef.palette.GolpeEditorPalette;

public class GolpeEditor extends GraphicalEditorWithFlyoutPalette {

	private RootComponent _root_model;
	private IFile file; 
	PropertySheetPage propertyPage;

	public GolpeEditor () {
		setEditDomain(new DefaultEditDomain(this));
		_root_model = new RootComponent();
		
	}

	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(_root_model);
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getGraphicalViewer().setEditPartFactory(new ComponentEditPartFactory());
	}

	@Override
	protected PaletteRoot getPaletteRoot() {

		return new GolpeEditorPalette();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		if (file == null)
			return;
		
		try {
			
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file.getName()));
			
			out.writeObject(this._root_model);
		
			out.close();
			
			getCommandStack().markSaveLocation();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		super.init(site, input);

		loadInput(input);
		
		this.setPartName(file.getName());
		
	}

	private void loadInput(IEditorInput input){

		if(input instanceof IFileEditorInput) {
			
			IFileEditorInput fileInput = (IFileEditorInput) input;
			file = fileInput.getFile();
			
			try {
				
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(file.getName()));
				this._root_model = (RootComponent) in.readObject();
				
				in.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} 
		}

	}

	@Override
	public void commandStackChanged(EventObject event) {
		firePropertyChange(PROP_DIRTY);
		super.commandStackChanged(event);
	}
}

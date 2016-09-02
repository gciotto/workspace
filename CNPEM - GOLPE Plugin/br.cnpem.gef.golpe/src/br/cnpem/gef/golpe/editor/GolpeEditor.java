package br.cnpem.gef.golpe.editor;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;

import emodel.EGolpeComponentModel;
import emodel.EGolpeModel;
import emodel.EmodelFactory;
import emodel.EmodelPackage;
import br.cnpem.gef.golpe.controller.GolpeEditPartFactory;
import br.cnpem.gef.palette.GolpeEditorPalette;

public class GolpeEditor extends GraphicalEditorWithFlyoutPalette {

	private EGolpeModel _root_model;
	private Resource _resource;

	public GolpeEditor () {
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(_root_model);
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getGraphicalViewer().setEditPartFactory(new GolpeEditPartFactory());
	}

	@Override
	protected PaletteRoot getPaletteRoot() {

		return new GolpeEditorPalette();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (_resource == null)
			return;
		
		try {
			_resource.save(null);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		super.init(site, input);

		EmodelPackage.eINSTANCE.eClass();
		ResourceSet _set = new ResourceSetImpl();

		if(input instanceof IFileEditorInput) {

			IFileEditorInput fileInput = (IFileEditorInput) input;

			IFile file = fileInput.getFile();

			_resource = _set.createResource(URI.createURI(file.getLocationURI().toString()));
			
			try {
				
				_resource.load(null);
				_root_model = (EGolpeModel) _resource.getContents().get(0);
				
			} catch(Exception e) {
				
				_root_model = EmodelFactory.eINSTANCE.createEGolpeModel();
								
				System.out.println("[Editor]" + e.getMessage());
			}
			
			
		}

	}

}

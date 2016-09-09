package br.cnpem.gef.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class IconPropertyDescriptor extends PropertyDescriptor {

	
	public IconPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		
		CellEditor fileEditor = new FileCellEditor(parent);
		
		if (getValidator() != null)
			fileEditor.setValidator(getValidator());
		
		return fileEditor;
	}
	
}

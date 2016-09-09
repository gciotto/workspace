package br.cnpem.gef.properties;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;

public class FileCellEditor extends DialogCellEditor {

	public FileCellEditor(Composite parent) {
		super(parent);
	}
	
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		FileDialog fileDialog = new FileDialog(cellEditorWindow.getShell(),  SWT.OPEN);
		
		fileDialog.setFilterExtensions(new String[] {"*.png" , "*.jpg", "*.gif" });		
		
		return fileDialog.open();
	}

}

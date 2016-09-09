package br.cnpem.gef.golpe.tools;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

public class ComponentNameCellEditorLocator implements CellEditorLocator {

	private Label _label_name;
	
	public ComponentNameCellEditorLocator(Label _label) {
		this._label_name = _label;
	}
	
	@Override
	public void relocate(CellEditor celleditor) {
		Text text = (Text) celleditor.getControl();
		Point pref = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	    Rectangle rect = _label_name.getTextBounds().getCopy();
	    _label_name.translateToAbsolute(rect);
	    text.setBounds(rect.x - 1, rect.y - 1, pref.x + 1, pref.y + 1); 
	}

}

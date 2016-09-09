package br.cnpem.gef.golpe.tools;

import org.eclipse.draw2d.Label;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;

public class ComponentDirectEditManager extends DirectEditManager {

	private Label _label_name;
	
	public ComponentDirectEditManager(GraphicalEditPart source,
			Class editorType, CellEditorLocator locator, Label feature) {
		super(source, editorType, locator, feature);
		this._label_name = feature;
	}

	@Override
	protected void initCellEditor() {
		String initString = this._label_name.getText();
		getCellEditor().setValue(initString);

	}

}

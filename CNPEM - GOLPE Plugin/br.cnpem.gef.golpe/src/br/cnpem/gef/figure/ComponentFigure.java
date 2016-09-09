package br.cnpem.gef.figure;


import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

public class ComponentFigure extends Figure {

	protected GridLayout _label_layout;
	public Figure _labels_container, interface_container;
	protected ImageFigure imageIcon;
	protected Label _label_name;
	protected RoundedRectangle rectangle;
	protected ConnectionAnchor _anchor;
	
	public ComponentFigure() {
		
		Figure _top_layer = new Figure();
				
		rectangle = new RoundedRectangle ();
		
		BorderLayout _top_layout = new BorderLayout();
				
		_top_layer.setLayoutManager(_top_layout);
		_top_layer.setBorder(new LineBorder(ColorConstants.gray, 2, Graphics.LINE_DASH ));
		
		this.imageIcon = new ImageFigure();
		this._label_name = new Label("");
		
		this._label_name.setTextAlignment(PositionConstants.CENTER);
		
		Font _f = new Font(Display.getCurrent(), "Monospace", 10, 0);
		
		this._label_name.setFont(_f);
		this._label_name.setTextAlignment(PositionConstants.CENTER);
		
		_top_layer.add (this.imageIcon, BorderLayout.LEFT);
		
		_label_layout = new GridLayout(2, true);
		
		 _labels_container = new Figure();
		_labels_container.setLayoutManager(_label_layout);
		_labels_container.add(_label_name);
		
		_top_layer.add (this.imageIcon, BorderLayout.LEFT);
		_top_layer.add (_labels_container, BorderLayout.CENTER);
		
		rectangle.setLayoutManager(new BorderLayout());
		rectangle.add(_top_layer, BorderLayout.TOP);
		rectangle.setBorder(new MarginBorder(8));
		rectangle.setLineStyle(SWT.LINE_SOLID);
		rectangle.setLineWidth(2);
		
		this.interface_container = new Figure();
		XYLayout gl = new XYLayout();
		this.interface_container.setLayoutManager(gl);
		
		rectangle.add(interface_container, BorderLayout.CENTER);
		
		BorderLayout _layout = new BorderLayout();
		this.setLayoutManager(_layout);
		this.add(rectangle, BorderLayout.CENTER);
		
		//this.setBackgroundColor(new Color(Display.getCurrent(), 204, 204, 255));
		this.setBackgroundColor(ColorConstants.white);
		this.setOpaque(true);
	}

	public ConnectionAnchor getConnectionAnchor(){
		
		if (this._anchor == null)
			this._anchor = new ChopboxAnchor(this);
		
		return this._anchor;
	}
	
	public ImageFigure getImageIcon() {
		return imageIcon;
	}
	
	public void setComponentName(String _name) {
		this._label_name.setText(_name);
	}
		
	public Label getNameLabel() {
		return this._label_name;
	}
	
}

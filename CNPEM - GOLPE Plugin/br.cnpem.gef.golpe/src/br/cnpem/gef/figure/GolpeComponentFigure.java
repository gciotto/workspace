package br.cnpem.gef.figure;


import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;

public class GolpeComponentFigure extends Figure {

	private ImageFigure imageIcon;
	private Label label;


	public GolpeComponentFigure() {
		
		Figure _top_layer = new Figure();
		
		_top_layer.setLayoutManager(new BorderLayout());
		
		this.imageIcon = new ImageFigure();
		this.label = new Label("");
		
		_top_layer.add (this.imageIcon, BorderLayout.LEFT);
		_top_layer.add (this.label, BorderLayout.CENTER);
		
		this.setBackgroundColor(ColorConstants.white);
		this.setBorder(new LineBorder(2));
		this.setOpaque(true);
		
		this.setLayoutManager(new BorderLayout());		

		this.add(_top_layer, BorderLayout.CENTER);
	}

	public ImageFigure getImageIcon() {
		return imageIcon;
	}
	
	public Label getLabel() {
		return label;
	}


}

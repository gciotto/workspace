package br.cnpem.gef.golpe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import br.cnpem.gef.properties.IconPropertyDescriptor;

public class Component extends Observable implements Comparable<Component>, Serializable, IPropertySource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Place location;
	protected String name, iconRoot;

	protected List<Connection> connections;
	
	private static final String PROPERTY_NAME = "br.cnpem.gef.model.views.name";
	private static final String PROPERTY_ICON = "br.cnpem.gef.model.views.icon";
	protected static final String ICON_DEFAULT = "/home/gciotto/eclipse-CSS4_4-LUNA/workspace/br.cnpem.gef.golpe/icon/generic.png";
	protected static final String NAME_DEFAULT = "New Generic Component";
	
	protected transient List<IPropertyDescriptor> propertyDescriptors;
	
	protected Point screenLocation;
	protected Dimension dimension;
	
	public Component(Place location, String name) {
		super();
		this.location = location;
		this.name = name;
		this.iconRoot = "/home/gciotto/eclipse-CSS4_4-LUNA/workspace/br.cnpem.gef.golpe/icon/generic.png";
		this.connections = new ArrayList<Connection>();
		
		this.screenLocation = new Point();
		this.dimension = new Dimension (200, 150);
	}

	
	public void update() {
		this.setChanged();
		this.notifyObservers();
	}
	
	public Dimension getDimensions() {
		return dimension;
	}

	public void setDimensions(Dimension dimension) {
		this.dimension = dimension;
		this.update();
	}

	public Point getScreenLocation() {
		return screenLocation;
	}

	public void setScreenLocation(Point screenLocation) {
		this.screenLocation = screenLocation;
		this.update();
	}

	public void setLocation(Place location) {
		this.location = location;
		this.update();
	}

	public void setName(String name) {
		this.name = name;
		this.update();
	}

	public void setIconRoot(String iconRoot) {
		this.iconRoot = iconRoot;
		this.update();
	}

	public Place getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}
	
	public String getIconRoot() {
		return iconRoot;
	}

	public Point getConstraints() {
		return this.screenLocation;
	}
	
	public void addGolpeConnection(Connection nConn) {
		this.connections.add(nConn);
		this.update();
	}
	
	
	@Override
	public int compareTo(Component o) {
		return this.name.compareTo(o.getName());
	}


	@Override
	public Object getEditableValue() {
		return this;
	}

	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		
		if (this.propertyDescriptors == null) {
			
			PropertyDescriptor nameDescriptor = new TextPropertyDescriptor(PROPERTY_NAME, "Name");
			nameDescriptor.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					return name;
				}
				
			});
						
			PropertyDescriptor iconDescriptor = new IconPropertyDescriptor (PROPERTY_ICON, "Icon");
			iconDescriptor.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					return iconRoot;
				}
			});
				
			this.propertyDescriptors = new ArrayList<IPropertyDescriptor>();
			this.propertyDescriptors.add(nameDescriptor);
			this.propertyDescriptors.add(iconDescriptor);
		}
		
		IPropertyDescriptor[] result = new IPropertyDescriptor[this.propertyDescriptors.size()];
		
		return this.propertyDescriptors.toArray(result);
	}


	@Override
	public Object getPropertyValue(Object id) {
		
		if (PROPERTY_ICON.equals(id))
			return getIconRoot();
		
		if (PROPERTY_NAME.equals(id))
			return getName();
				
		return null;
	}


	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void resetPropertyValue(Object id) {
		
		if (PROPERTY_ICON.equals(id))
			setIconRoot(ICON_DEFAULT);
		
		if (PROPERTY_NAME.equals(id))
			setName(NAME_DEFAULT);	
	}


	@Override
	public void setPropertyValue(Object id, Object value) {
		if (PROPERTY_ICON.equals(id))
			setIconRoot((String) value);
		
		if (PROPERTY_NAME.equals(id))
			setName((String) value);	
				
	}
}

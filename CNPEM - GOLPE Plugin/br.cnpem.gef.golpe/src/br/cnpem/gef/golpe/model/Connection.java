package br.cnpem.gef.golpe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import br.cnpem.gef.properties.IconPropertyDescriptor;

public class Connection implements Comparable<Connection>, Serializable, IPropertySource {

	private static final long serialVersionUID = 1L;

	public static int EXTREMITY1 = 1, EXTREMITY2 = 2;
	private static final String PROPERTY_PORT = "br.cnpem.gef.model.views.connection.port";
	
	protected Component extremity1, extremity2;
	private RootComponent rootModel;
	private int connectionPort;
	private transient List<IPropertyDescriptor> propertyDescriptors;

	public void setRoot(RootComponent r) {
		this.rootModel = r;
		connectionPort = 10;
	}

	public RootComponent getRoot() {
		return this.rootModel;
	}


	public Connection() {
		this.extremity1 = null;
		this.extremity2 = null;
	}

	public Connection(Component extremity1, Component extremity2) {
		this.extremity1 = extremity1;
		this.extremity2 = extremity2;
	}

	public Component getExtremity1() {
		return extremity1;
	}

	public void setExtremity1(Component extremity1) {
		this.extremity1 = extremity1;
	}

	public Component getExtremity2() {
		return extremity2;
	}

	public void setExtremity2(Component extremity2) {
		this.extremity2 = extremity2;
	}

	@Override
	public int compareTo(Connection o) {

		if ((o.extremity1 == this.extremity1 && o.extremity2 == this.extremity2) ||
				(o.extremity1 == this.extremity2 && o.extremity2 == this.extremity1) )
			return 1;

		return 0;
	}

	@Override
	public Object getEditableValue() {
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		
		if (this.propertyDescriptors == null) {

			PropertyDescriptor portDescriptor = new TextPropertyDescriptor(PROPERTY_PORT, "Port");
			portDescriptor.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					return Integer.toString(connectionPort);
				}

			});


			this.propertyDescriptors = new ArrayList<IPropertyDescriptor>();
			this.propertyDescriptors.add(portDescriptor);
		}

		IPropertyDescriptor[] result = new IPropertyDescriptor[this.propertyDescriptors.size()];

		return this.propertyDescriptors.toArray(result);// TODO Auto-generated method stub
	}

	@Override
	public Object getPropertyValue(Object id) {
		
		if (PROPERTY_PORT.equals(id)) 
			return Integer.toString(this.connectionPort);
		
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return true;
	}

	@Override
	public void resetPropertyValue(Object id) {
		
		if (PROPERTY_PORT.equals(id)) 
			this.connectionPort = 10;

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		
		if (PROPERTY_PORT.equals(id)) 
			this.connectionPort = Integer.valueOf((String)value);

	}

}

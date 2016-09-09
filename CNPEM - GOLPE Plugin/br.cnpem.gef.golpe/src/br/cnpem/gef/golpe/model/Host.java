package br.cnpem.gef.golpe.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class Host extends Component {

	private static final long serialVersionUID = 1L;
	private transient PropertyDescriptor netinterfaceDescriptor; 
	protected List<NetworkInterface> n_interfaces;
	protected int number_interfaces;
	protected static final String PROPERTY_INTERFACE = "br.cnpem.gef.model.views.interface";

	public Host(Place location, String name) {
		super(location, name);
		this.n_interfaces = new ArrayList<NetworkInterface>();
		this.number_interfaces = 1;

	}

	public void setMaxNInterface(int n){
		this.number_interfaces = n;
		update();
	}

	public int getMaxNInterface(){
		return number_interfaces;
	}

	public void addNetworkInterface(NetworkInterface n) {		
		this.n_interfaces.add(n);

		for (NetworkInterface i : this.n_interfaces) 
			i.update();

		update();
	}

	public void removeNetworkInterface(NetworkInterface n) {
		this.n_interfaces.remove(n);

		for (NetworkInterface i : this.n_interfaces) 
			i.update();

		update();
	}

	public List getNetworkInterfaces () {
		return this.n_interfaces;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		
		super.getPropertyDescriptors();

		if (netinterfaceDescriptor == null) {

			netinterfaceDescriptor = new TextPropertyDescriptor(PROPERTY_INTERFACE, "Number of network interfaces");
			
			netinterfaceDescriptor.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					return Integer.toString(number_interfaces);
				}
				
			});

			this.propertyDescriptors.add(netinterfaceDescriptor);
		}

		IPropertyDescriptor[] result = new IPropertyDescriptor[this.propertyDescriptors.size()];

		return this.propertyDescriptors.toArray(result);

	}

	@Override
	public void setPropertyValue(Object id, Object value) {

		if (PROPERTY_INTERFACE.equals(id))
			this.setMaxNInterface(Integer.valueOf((String) value));

		super.setPropertyValue(id, value);
	}

	@Override
	public Object getPropertyValue(Object id) {

		if (PROPERTY_INTERFACE.equals(id))
			return Integer.toString(this.number_interfaces);

		return super.getPropertyValue(id);
	}

}

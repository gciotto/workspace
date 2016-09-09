package br.cnpem.gef.golpe.model;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class Switch extends Component {

	private static final long serialVersionUID = 1L;
	protected int numberPorts;
	protected transient TextPropertyDescriptor nPortsDescriptor;
	protected static final String PROPERTY_NPORTS = "br.cnpem.gef.model.views.switch.num_ports";

	public Switch(Place location, String name) {
		super(location, name);

		this.iconRoot = "/home/gciotto/eclipse-CSS4_4-LUNA/workspace/br.cnpem.gef.golpe/icon/switch.png";
		this.numberPorts = 8;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		super.getPropertyDescriptors();

		if (nPortsDescriptor == null) {

			nPortsDescriptor = new TextPropertyDescriptor(PROPERTY_NPORTS, "Number of network interfaces");

			nPortsDescriptor.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					return Integer.toString(numberPorts);
				}

			});

			this.propertyDescriptors.add(nPortsDescriptor);
		}

		IPropertyDescriptor[] result = new IPropertyDescriptor[this.propertyDescriptors.size()];

		return this.propertyDescriptors.toArray(result);

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		
		if (PROPERTY_NPORTS.equals(id))
			this.setNumberPorts(Integer.valueOf((String)value));
		
		super.setPropertyValue(id, value);
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if (PROPERTY_NPORTS.equals(id))
			return Integer.toString(this.getNumberPorts());
		
		return super.getPropertyValue(id);
	}
	
	@Override
	public void resetPropertyValue(Object id) {
		
		if (PROPERTY_NPORTS.equals(id))
			this.setNumberPorts(16);
		
		super.resetPropertyValue(id);
	}
	
	public int getNumberPorts() {
		return numberPorts;
	}

	public void setNumberPorts(int numberPorts) {
		this.numberPorts = numberPorts;
		update();
	}
	
	

}

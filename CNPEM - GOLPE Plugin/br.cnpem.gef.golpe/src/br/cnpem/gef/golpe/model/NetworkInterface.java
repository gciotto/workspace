package br.cnpem.gef.golpe.model;

import java.io.Serializable;
import java.util.Observable;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import br.cnpem.gef.properties.IconPropertyDescriptor;

public class NetworkInterface extends Observable implements Serializable, IPropertySource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ip_address, plug, name;
	private boolean isDHCP;

	private transient IPropertyDescriptor[] propertyDescriptors;

	private static final String PROPERTY_NAME = "br.cnpem.gef.model.views.networkinterface.name";
	private static final String PROPERTY_PLUG = "br.cnpem.gef.model.views.networkinterface.plug";
	private static final String PROPERTY_DHCP = "br.cnpem.gef.model.views.networkinterface.dhcp";
	private static final String PROPERTY_IP = "br.cnpem.gef.model.views.networkinterface.ip";

	public NetworkInterface(String ip_address, String name, String plug, boolean isDHCP) {
		super();
		this.ip_address = ip_address;
		this.plug = plug;
		this.isDHCP = isDHCP;
		this.name = name;
	}

	public void update (){
		this.setChanged();
		this.notifyObservers();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		update();
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
		update();
	}

	public String getPlug() {
		return plug;
	}

	public void setPlug(String plug) {
		this.plug = plug;
		update();
	}

	public boolean isDHCP() {
		return isDHCP;
	}

	public void setDHCP(boolean isDHCP) {
		this.isDHCP = isDHCP;
		update();
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (this.propertyDescriptors == null) {

			PropertyDescriptor nameDescriptor = new TextPropertyDescriptor(PROPERTY_NAME, "Interface name (e.g. eth0)");
			nameDescriptor.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					return name;
				}

			});

			PropertyDescriptor ipDescriptor = new TextPropertyDescriptor (PROPERTY_IP, "IP Address");
			ipDescriptor.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					return ip_address;
				}
			});

			PropertyDescriptor plugDescriptor = new TextPropertyDescriptor (PROPERTY_PLUG, "Plug");
			plugDescriptor.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					return plug;
				}
			});


			this.propertyDescriptors = new IPropertyDescriptor[] { nameDescriptor, ipDescriptor, plugDescriptor };
		}

		return this.propertyDescriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {

		if (PROPERTY_DHCP.equals(id))
			return this.isDHCP;

		if (PROPERTY_IP.equals(id))
			return this.ip_address;

		if (PROPERTY_NAME.equals(id))
			return this.name;

		if (PROPERTY_PLUG.equals(id))
			return this.plug;

		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void resetPropertyValue(Object id) {
		if (PROPERTY_DHCP.equals(id))
			this.setDHCP(false);

		if (PROPERTY_IP.equals(id))
			this.setIp_address("192.168.7.2");

		if (PROPERTY_NAME.equals(id))
			this.setName("eth0");

		if (PROPERTY_PLUG.equals(id))
			this.setPlug("Ethernet");

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (PROPERTY_DHCP.equals(id))
			this.setDHCP((boolean) value);

		if (PROPERTY_IP.equals(id))
			this.setIp_address((String) value);

		if (PROPERTY_NAME.equals(id))
			this.setName((String) value);

		if (PROPERTY_PLUG.equals(id))
			this.setPlug((String) value);
	}

}

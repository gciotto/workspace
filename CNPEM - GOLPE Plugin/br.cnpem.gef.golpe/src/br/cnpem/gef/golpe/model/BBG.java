package br.cnpem.gef.golpe.model;

public class BBG extends Host {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static final String ICON_DEFAULT = "/home/gciotto/eclipse-CSS4_4-LUNA/workspace/br.cnpem.gef.golpe/icon/bbg_board.png";
	protected static final String NAME_DEFAULT = "New BBG";

	public BBG(Place location, String name) {
		super(location, name);
		this.iconRoot = "/home/gciotto/eclipse-CSS4_4-LUNA/workspace/br.cnpem.gef.golpe/icon/bbg_board.png";
		this.number_interfaces = 2;
	}

}
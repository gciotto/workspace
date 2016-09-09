package br.cnpem.gef.golpe.model;

public class BBB extends Host {

	protected static final String ICON_DEFAULT = "/home/gciotto/eclipse-CSS4_4-LUNA/workspace/br.cnpem.gef.golpe/icon/bbb_board.png";
	protected static final String NAME_DEFAULT = "New BBB";
	
	
	
	public BBB(Place location, String name) {
		super(location, name);
		this.iconRoot = "/home/gciotto/eclipse-CSS4_4-LUNA/workspace/br.cnpem.gef.golpe/icon/bbb_board.png";
		this.number_interfaces = 2;
	}

}

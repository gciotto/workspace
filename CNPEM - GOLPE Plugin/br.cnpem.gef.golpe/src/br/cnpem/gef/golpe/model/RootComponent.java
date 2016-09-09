package br.cnpem.gef.golpe.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class RootComponent extends Observable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4971938700104178584L;
	private List<Component> nodes;
	private List<Connection> connections;
	
	public RootComponent() {
		
		this.nodes = new ArrayList<Component> ();
		this.connections = new ArrayList<Connection>();
	}
	
	public List<Component> getComponentList () {
				
		return this.nodes;
	}
	
	private void update() {
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public void addComponent (Component nNode) {
		
		this.nodes.add(nNode);
		update();
	}
	
	public void removeComponent (Component rNode) {
		
		this.nodes.remove(rNode);
		update();	
	}
	
	public void addConnection (Connection nNode) {
		
		this.connections.add(nNode);
		
		nNode.getExtremity1().update();
		nNode.getExtremity2().update();
		
		update();
	}
	
	public void removeConnection (Connection rNode) {
		
		this.connections.remove(rNode);
		
		rNode.getExtremity1().update();
		rNode.getExtremity2().update();
		
		update();	
	}
	
	public void clearModel() {
		this.nodes.clear();
		update();
	}

	public List<Connection> getConnectionsWith (Component c) {
		
		List<Connection> l = new ArrayList<Connection>();
		
		for (Connection conn : this.connections)
			if (conn.getExtremity1() == c || conn.getExtremity2() == c)
				l.add(conn);
		
		return l;
	}
	
	public List<Connection> getConnectionsWithAt (Component c, int extremity) {
		
		List<Connection> l = new ArrayList<Connection>();
		
		for (Connection conn : this.connections)
			if (extremity == Connection.EXTREMITY1 && conn.getExtremity1() == c)
				l.add(conn);
		
			else if (extremity == Connection.EXTREMITY2 && conn.getExtremity2() == c)
				l.add(conn);
		
		return l;
	}
	
	
	public boolean connectionExists (Component source, Component target) {
		
		for (Connection conn : this.connections)
			if ((conn.getExtremity1() == source && conn.getExtremity2() == target) ||
				(conn.getExtremity2() == source && conn.getExtremity1() == target))
				return true;
		
		return false;
	}
	
}

package test;

import java.awt.GridLayout;

import javax.swing.JFrame;

import gui.Digint;
import gui.HP232;
import gui.Statfnt;

public class TestInterfaceFrame extends JFrame {

	public TestInterfaceFrame() {
		
		setSize(1020, 320);
		
		this.getContentPane().setLayout(new GridLayout(0, 3, 20, 20));
		
		this.getContentPane().add(new Statfnt());
		this.getContentPane().add(new HP232());
		this.getContentPane().add(new Digint());
		
		setVisible(true);
	}
	
}

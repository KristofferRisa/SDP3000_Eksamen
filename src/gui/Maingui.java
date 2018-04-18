package gui;

import gui.domain.Mainmenu;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import controller.Controller;

public class Maingui extends JFrame {

	Centerpane centerpane;
	Northpane northpane;
	Southpane southpane;
	
	public Maingui() {
		
		Controller.init(this);
		
		setTitle("Enkel tekstbehandler med html");
		
		add(northpane = new Northpane(),BorderLayout.NORTH);
		add(centerpane = new Centerpane());
		add(southpane = new Southpane(), BorderLayout.SOUTH);
		
		setJMenuBar(new Mainmenu());
		
		setSize(600,600);
		setLocationRelativeTo(null);
		setVisible(true);
		
		setDividers();
		
	}

	private void setDividers() {
		centerpane.setDividers();		
	}

	public void open() {		
		centerpane.open();
	}

	public void save() {
		centerpane.save();		
	}	

	public void paste() {
		centerpane.paste();		
	}

	public void testhtml() {
		centerpane.testhtml();		
	}

	public void toggleHtmlView(boolean toggle) {
		centerpane.toggleHtmlView(toggle);
	}
	
	
}

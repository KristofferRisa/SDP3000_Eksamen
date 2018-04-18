package gui;

import gui.domain.Domainview;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class Centerpane extends JPanel  {	
		
	Domainview domainview;	
	
	public Centerpane() {
		setLayout(new BorderLayout());		
		add(domainview = new Domainview());				
	}

	public void setDividers() {		
		domainview.setDividers();
	}

	public void open() {		
		domainview.open();
	}

	public void save() {
		domainview.save();
		
	}

	public void paste() {
		domainview.paste();
		
	}

	public void testhtml() {
		domainview.testhtml();
		
	}

	public void toggleHtmlView(boolean toggle) {
		domainview.toggleHtmlView(toggle);
	}
	

}

package gui;

import gui.domain.Commandpane;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class Southpane extends JPanel {
	
	public Southpane() {
		add(new Commandpane(),BorderLayout.SOUTH);
	}

}

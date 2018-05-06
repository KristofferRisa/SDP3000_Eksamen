package gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import gui.domain.Toolpane;

public class Southpane extends JPanel {
	
	public Southpane() {
		add(new Toolpane(),BorderLayout.SOUTH);
	}

}

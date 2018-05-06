package gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import gui.domain.Toolpane;

public class Southpane extends JPanel {
	
	private static final long serialVersionUID = -6321000967445131992L;

	public Southpane() {
		add(new Toolpane(),BorderLayout.SOUTH);
	}

}

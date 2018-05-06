package gui.domain;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import controller.Controller;

public class Toolpane extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = -7579333997356940204L;
	
	JButton open = new JButton("| Åpne |");
	JButton se = new JButton("| Se i nettleser |");
	JButton save = new JButton("| Eksporter |");
	JButton exit = new JButton("| Avslutt |");
	
	JToolBar toolbar = new JToolBar(); 
	
	public Toolpane() {
		open.getPreferredSize();
		se.getPreferredSize();
		save.getPreferredSize();
		exit.getPreferredSize();
		
		((JButton)toolbar.add(open)).addActionListener(this);
		((JButton)toolbar.add(save)).addActionListener(this);
		((JButton)toolbar.add(se)).addActionListener(this);
		((JButton)toolbar.add(exit)).addActionListener(this);
		add(toolbar);
	}
	
	public void actionPerformed(ActionEvent arg) {		
		if (arg.getSource()==open) Controller.open();
		if (arg.getSource()==save) Controller.save();
		if (arg.getSource()==exit) Controller.stop();
		if(arg.getSource() == se) Controller.se();
	}
	
	
}

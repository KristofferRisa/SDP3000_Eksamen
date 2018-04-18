package gui.domain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Controller;

public class Commandpane extends JPanel implements ActionListener {
	
	JButton open = new JButton("Åpne fil");
	JButton save = new JButton("Lagre fil");
	JButton exit = new JButton("Avslutt");
	
	public Commandpane() {
		add(open);
		add(save);
		add(exit);
		
		open.addActionListener(this);
		save.addActionListener(this);
		exit.addActionListener(this);		
	}

	public void actionPerformed(ActionEvent arg) {		
		if (arg.getSource()==open) Controller.open();
		if (arg.getSource()==save) Controller.save();
		if (arg.getSource()==exit) Controller.stop();
	}
	
	
}

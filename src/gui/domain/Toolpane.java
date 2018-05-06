package gui.domain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import controller.Controller;

public class Toolpane extends JPanel implements ActionListener {
	
	JButton open = new JButton("Åpne",new ImageIcon("load.png"));
	JButton se = new JButton("Se i nettleser",new ImageIcon("load.png"));
	JButton save = new JButton("Lagre",new ImageIcon("save.png"));
	JButton exit = new JButton("Avslutt",new ImageIcon("exit.png"));
	
	JToolBar toolbar = new JToolBar(); 
	
	public Toolpane() {
		((JButton)toolbar.add(open)).addActionListener(this);
		((JButton)toolbar.add(save)).addActionListener(this);
		((JButton)toolbar.add(exit)).addActionListener(this);
		((JButton)toolbar.add(se)).addActionListener(this);
		add(toolbar);
	}
	
	public void actionPerformed(ActionEvent arg) {		
		if (arg.getSource()==open) Controller.open();
		if (arg.getSource()==save) Controller.save();
		if (arg.getSource()==exit) Controller.stop();
		if(arg.getSource() == se) Controller.se();
	}
	
	
}

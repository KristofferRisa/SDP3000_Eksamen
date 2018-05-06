/*
 * Created on 19.mar.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui.domain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import controller.Controller;

public class Mainmenu extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = -3090867748585220252L;

	JMenuItem open = new JMenuItem("load");
	JMenuItem save = new JMenuItem("save");
	JMenuItem exit = new JMenuItem("exit");
	JMenuItem paste = new JMenuItem("paste");
	JMenuItem test = new JMenuItem("test");

	JCheckBoxMenuItem toggleAutoForHtmlRendering = new JCheckBoxMenuItem("Vis HTML",false);
	
	public Mainmenu() {
		// list of verticals
		JMenu file = new JMenu("file");
		JMenu edit = new JMenu("edit");
		JMenu tools = new JMenu("tools");
		JMenu search = new JMenu("search");
		JMenu windows = new JMenu("windows");
		JMenu help = new JMenu("help");

		// file
		file.add(open).addActionListener(this);
		file.add(save).addActionListener(this);
		file.add(exit).addActionListener(this);
		

		// edit
		edit.add(paste).addActionListener(this);
		

		// tools
		tools.add(test).addActionListener(this);
		tools.add(toggleAutoForHtmlRendering).addActionListener(this);

		// windows

		// help

		add(file);
		add(edit);
		add(tools);
		add(search);
		add(windows);
		add(help);
		
		toggleHtmlView();
	}

	public void actionPerformed(ActionEvent arg) {
		if (arg.getSource() == open)
			Controller.open();
		if (arg.getSource() == save)
			Controller.save();
		if (arg.getSource() == exit)
			Controller.stop();
		if (arg.getSource() == paste)
			Controller.paste();
		if (arg.getSource() == test)
			Controller.testhtml();
		if(arg.getSource() == toggleAutoForHtmlRendering)
			toggleHtmlView();
	}

	private void toggleHtmlView() {
		Controller.toggleHtmlView(toggleAutoForHtmlRendering.isSelected());
	}

}

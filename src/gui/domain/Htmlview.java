package gui.domain;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Htmlview extends JPanel {

	private static final long serialVersionUID = 1L;
	
	JLabel text = new JLabel();

	public Htmlview() {		
		setLayout(new BorderLayout()); 
		add(new JScrollPane(text));
	}

	public void setText(String taggedtext) {
		text.setText(taggedtext);
	}

}

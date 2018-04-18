package debugger;

import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.bind.DatatypeConverter;


/*
 * What about using JTextPane here!!
 */


public class Debugwindow extends JFrame {
	
	private JTextArea textArea;
	private StyledDebugger pane, visitor, hex = null;
	
	private static int count = 0;
	private static int line = 0;
	
	final int WIDTH = 350;
	final int HEIGHT = 200;
	
//	private TwoColumn list;
	private Tabpane tabs;

	public Debugwindow(String title) {
		setTitle(title);
		add(tabs = new Tabpane());
		tabs.add("simple", textArea = new JTextArea());
		tabs.add("pane", new JScrollPane(pane = new StyledDebugger()));
		
		
		setBounds(count*WIDTH, 10, WIDTH,HEIGHT);
		setVisible(true);
		count++;
	}
	
	public Debugwindow(String title, int x , int y) {
		setTitle(title);
		add(tabs = new Tabpane());
		tabs.add("simple", textArea = new JTextArea());
		tabs.add("pane", new JScrollPane(pane = new StyledDebugger()));
		tabs.add("visitor", new JScrollPane(visitor = new StyledDebugger()));
		tabs.add("hex", new JScrollPane(hex = new StyledDebugger()));
		
		setBounds(x*WIDTH, y*HEIGHT, WIDTH,HEIGHT);
		setVisible(true);
		
	}
	
	
	
	public Debugwindow() throws HeadlessException {
		super();
	}

	public void write(String s) {
		textArea.append(s);
	}
	
	public void writeLn(String s) {
		textArea.append("\n" + s);
	}

//	public void toList(String a, String b) {
//		if (list == null) {
//			list = new TwoColumn();
//			add(list);
//		}
//		list.add(a, b);
//	}

	public void toPane(String s) {
		pane.writeln(s);
	}

	public void paneToHighlight(String string) {
		pane.highlight(string);
	}

	public void paneDrawString(String string, Float xpos) {
		pane.drawString(string, xpos);
		
	}

	public void paneNormalgreen(String s) {
		pane.normalgreen(s);
	}

	public void paneBold(String s) {
		pane.bold(s);
	}

	public void paneBlue(String s) {
		pane.blue(s);
	}

	public void paneSmall(String s) {
		pane.small(s);
	}

	public void visitSmall(String s) {
		visitor.small(s);
	}
	
	

//	public void visitBlue(String s) {
//		visitor.blue(s);
//	}

	private byte[] bb= new byte[1];
	
	public void hex(byte[] n) {
		hex.small("----- " + n.length +  " bytes --------------------");
		String h = "";
		String c = "";
		for (int i = 0; i < n.length; i++) {
			if (i % 16 == 0) {
				hex.small(h + " " + c);
				h = "";
				c = "";
			}
			h = h + padded(n[i]); 
			c = c + printable(n[i]);						
		}
		while (h.length() < (16*3)) {
			h += " ";
		}
		hex.small(h + " " + c);		
	}

	private String padded(byte s) {
		String a = Integer.toHexString(s & 0xFF);
		return  "00".substring(a.length()) + a + " ";
	}
	
	private char printable(byte b) {
		switch (b) {
			case 7 : b = '.';
			case 9 : b = '.';
			case 10: b = '.'; 
			case 13: b = '.';
		}
		return (char)b;	
	}

	public void visitBlue(String s) {
		visitor.blue(s);
	}
	
}

package tabbeddebugger;


import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class StyledDebugger extends JTextPane {
	
		
	
	private StyledDocument doc;

	public StyledDebugger() {
		doc = getStyledDocument();
		addStylesToDocument(doc);
	/*
		 try {
			doc.insertString(doc.getLength(), "Dette ",
			         doc.getStyle("italic"));
			

			
			doc.insertString(doc.getLength(), "er en test",
			         doc.getStyle("bold"));
			
			
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	
	  protected void addStylesToDocument(StyledDocument doc) {
	        //Initialize some styles.
	        Style def = StyleContext.getDefaultStyleContext().
	                        getStyle(StyleContext.DEFAULT_STYLE);

	        Style regular = doc.addStyle("regular", def);
	        StyleConstants.setFontFamily(def, "Monospaced");
	        
//	        Style serif = doc.addStyle("serif", def);
//	        StyleConstants.setFontFamily(def, "Serif");

	        Style s = doc.addStyle("italic", regular);
	        StyleConstants.setItalic(s, true);
	        
	        s = doc.addStyle("italicred", regular);
	        StyleConstants.setItalic(s, true);
	        StyleConstants.setForeground(s, Color.RED);
	        
	        s = doc.addStyle("normalgreen", regular);
	        StyleConstants.setForeground(s, new Color(50,230,50));

	        s = doc.addStyle("blue", regular);
	        StyleConstants.setForeground(s, Color.BLUE);

	        s = doc.addStyle("bold", regular);
	        StyleConstants.setBold(s, true);	        
	        StyleConstants.setForeground(s, Color.MAGENTA);	        

	        s = doc.addStyle("small", regular);
	        StyleConstants.setFontSize(s, 10);

	        s = doc.addStyle("large", regular);
	        StyleConstants.setFontSize(s, 16);	       
	       
	    }


	public void writeln(String s) {
		try {
			doc.insertString(doc.getLength(),s + "\n",
			         doc.getStyle("regular"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(String s) {
		try {
			doc.insertString(doc.getLength(),s ,
			         doc.getStyle("regular"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void highlight(String s) {
		try {
			doc.insertString(doc.getLength(),s + "\n",
			         doc.getStyle("italicred"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void normalgreen(String s) {
		try {
			doc.insertString(doc.getLength(),s + "\n",
			         doc.getStyle("normalgreen"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void bold(String s) {
		try {
			doc.insertString(doc.getLength(),s + "\n",
			         doc.getStyle("bold"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void drawString(String s, Float xpos) {
		// String insertion = posformat(xpos) + String.format("%-120s", " [" + s + "]\n");
		String insertion = posformat(xpos) + "\t\t\t\t\t" + String.format("%s", "[" + s + "]\n");
		try {
			doc.insertString(doc.getLength(), insertion,
			         doc.getStyle("normalgreen"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String posformat(Float f) {
		return String.format(java.util.Locale.US,"%11.0f",f);
	}


	public void blue(String s) {
		try {
			doc.insertString(doc.getLength(),s + "\n",
			         doc.getStyle("blue"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void small(String s) {
		try {
			doc.insertString(doc.getLength(),s + "\n",
			         doc.getStyle("small"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

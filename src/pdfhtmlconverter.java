import javax.swing.UIManager;

import gui.Maingui;

public class pdfhtmlconverter {

	public static void main(String[] args) {
		try {
		     UIManager.setLookAndFeel(
		       UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			
		}		
	
		new Maingui();
	
	}

}

import javax.swing.UIManager;

import gui.Maingui;

public class pdfhtmlconverter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		new ApplicationFrame();
		try {
		     UIManager.setLookAndFeel(
		       UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			
		}		
	
		new Maingui();
	
	}

}

import java.io.File;

import logic.ObjectInvestigator;

public class Investigator {

	public static void main(String[] args) {
		File f = new File("gressklipper.pdf");
		// File f = new File("smtp.pdf");
		new ObjectInvestigator(f);
	}

}

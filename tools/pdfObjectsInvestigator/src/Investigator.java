import java.io.File;

import logic.ObjectInvestigator;

public class Investigator {

	public static void main(String[] args) {
		File f = new File("test.pdf");
		System.out.println("Java version:" + System.getProperty("java.version"));
		new ObjectInvestigator(f);
	}

}

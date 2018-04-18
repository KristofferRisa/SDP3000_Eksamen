import java.util.Formatter;

import javax.xml.bind.DatatypeConverter;

public class HurtigTester {
	
	public static void main(String[] arg) {
		/*byte[]  b = {100, 30, 9, 20};
		String s = DatatypeConverter.printHexBinary(b );
		System.out.println(s);
		
		String t = "e";
		
		
		System.out.println(String.format("%5s",t));
		
		// System.out.printf("%05s",t);
		Formatter f;
		String unpadded = "2"; 
		String padded = "00".substring(unpadded.length()) + unpadded;
		System.out.println(padded);
		*/
		
		System.out.println(byteToHex((byte)127));
		
		
		
		
	}
	
	public static String byteToHex(byte b) {
	    int i = b & 0xFF;
	    return Integer.toHexString(i);
	  }

}

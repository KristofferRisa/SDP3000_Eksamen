package logic;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSBoolean;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInputStream;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNull;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.cos.ICOSVisitor;

public class Visitor implements ICOSVisitor {
	
	public Visitor() {
		
	}

	@Override
	public Object visitFromArray(COSArray p) throws IOException {
//		System.out.println("visitFromArray");
//		System.out.println("Size:" + p.size());
		return null;
	}

	@Override
	public Object visitFromBoolean(COSBoolean p) throws IOException {
//		System.out.println("visitFromBoolean");
		return null;
	}

	@Override
	public Object visitFromDictionary(COSDictionary p) throws IOException {
//		System.out.println("visitFromDictionary");
//		System.out.println("Size:" + p.size());
		return null;
	}

	@Override
	public Object visitFromDocument(COSDocument p) throws IOException {
		System.out.println("visitFromDocument");
		System.out.println("Catalog:" + p.getCatalog());
		System.out.println("Test " + p.getVersion());
		return null;
	}

	@Override
	public Object visitFromFloat(COSFloat p) throws IOException {
//		System.out.println("visitFromFloat");
//		System.out.println("Float Value:" + p.floatValue());
		return null;
	}

	@Override
	public Object visitFromInt(COSInteger p) throws IOException {
//		System.out.println("visitFromInt");
//		System.out.println("Int Value:" + p.intValue());
		return null;
	}

	@Override
	public Object visitFromName(COSName p) throws IOException {		
//		System.out.println("visitFromName");
//		System.out.println("getName:" + p.getName());
		return null;
	}

	@Override
	public Object visitFromNull(COSNull p) throws IOException {		
//		System.out.println("visitFromNull");
		return null;
	}

	@Override
	public Object visitFromStream(COSStream p) throws IOException {	
		/* System.out.println(p.toTextString()); // same as inputstream */
		for (Map.Entry<COSName, COSBase> es : p.entrySet()) {
			System.out.println("key:" + es.getKey() + " value:" + es.getValue()); 
			}
		
//		displayInputstream(p);		
//		displayRawAsHex(p);
//		
		return null;
	}

	private void displayInputstream(COSStream p) throws IOException {
		COSInputStream stream = p.createInputStream();
		BufferedReader br  = new BufferedReader(new InputStreamReader(stream));
		String l;
		while ((l = br.readLine()) != null) {
			System.out.println(l);
		}
	}

	private void displayRawAsHex(COSStream p) throws IOException {
		InputStream raw = p.createRawInputStream();
		InputStreamReader isr = new InputStreamReader(raw);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int n;
		while ((n = isr.read()) != -1) {		
			bos.write(n);
			}
		bos.close();
	}

	@Override
	public Object visitFromString(COSString p) throws IOException {
//		System.out.println("visitFromString");
//		System.out.println("String:" + p.getString());
		return null;
	}

}

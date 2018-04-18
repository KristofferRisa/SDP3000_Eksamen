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

import tabbeddebugger.Debugwindow;

public class Visitor implements ICOSVisitor {
	
	Debugwindow dw = null;
	
	public Visitor(Debugwindow w) {
		dw = w;
	}

	@Override
	public Object visitFromArray(COSArray p) throws IOException {
		dw.visitSmall("visitFromArray");
		dw.visitSmall("Size:" + p.size());
		return null;
	}

	@Override
	public Object visitFromBoolean(COSBoolean p) throws IOException {
		dw.visitSmall("visitFromBoolean");
		return null;
	}

	@Override
	public Object visitFromDictionary(COSDictionary p) throws IOException {
		dw.visitSmall("visitFromDictionary");
		dw.visitSmall("Size:" + p.size());
		return null;
	}

	@Override
	public Object visitFromDocument(COSDocument p) throws IOException {
		dw.visitSmall("visitFromDocument");
		dw.visitSmall("Catalog:" + p.getCatalog());
		return null;
	}

	@Override
	public Object visitFromFloat(COSFloat p) throws IOException {
		dw.visitSmall("visitFromFloat");
		dw.visitSmall("Float Value:" + p.floatValue());
		return null;
	}

	@Override
	public Object visitFromInt(COSInteger p) throws IOException {
		dw.visitSmall("visitFromInt");
		dw.visitSmall("Int Value:" + p.intValue());
		return null;
	}

	@Override
	public Object visitFromName(COSName p) throws IOException {		
		dw.visitSmall("visitFromName");
		dw.visitSmall("getName:" + p.getName());
		return null;
	}

	@Override
	public Object visitFromNull(COSNull p) throws IOException {		
		dw.visitSmall("visitFromNull");
		return null;
	}

	@Override
	public Object visitFromStream(COSStream p) throws IOException {	
		dw.visitBlue("visitFromStream");
		dw.visitBlue("COSStream is a COSDictionary, size:" + p.size());
		dw.visitBlue("Length of encoded stream:" + p.getLength() + " corresponds with hex bytes");
		/* dw.visitSmall(p.toTextString()); // same as inputstream */
		for (Map.Entry<COSName, COSBase> es : p.entrySet()) {
			dw.visitSmall("key:" + es.getKey() + " value:" + es.getValue()); 
			}
		
		displayInputstream(p);		
		displayRawAsHex(p);
		
		return null;
	}

	private void displayInputstream(COSStream p) throws IOException {
		COSInputStream stream = p.createInputStream();
		BufferedReader br  = new BufferedReader(new InputStreamReader(stream));
		String l;
		while ((l = br.readLine()) != null) {
			dw.visitSmall(l);
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
		dw.hex(bos.toByteArray());
	}

	@Override
	public Object visitFromString(COSString p) throws IOException {
		dw.visitSmall("visitFromString");
		dw.visitSmall("String:" + p.getString());
		return null;
	}

}

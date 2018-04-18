package logic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSBoolean;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNull;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import tabbeddebugger.Debugwindow;

public class ObjectInvestigator {


	
	final Class[] CLASS_LIST = {COSArray.class, 
			COSBoolean.class, 
			COSDictionary.class,
			COSDocument.class, 
			COSName.class, 
			COSNull.class, 
			COSNumber.class, 
			COSObject.class, 
			COSString.class,
			COSStream.class    // Not a COSBase
			};
	
	ArrayList<Class> CLASSES = new ArrayList<Class>(Arrays.asList(CLASS_LIST));
	
	
	// PDPageTree org.apache.pdfbox.pdmodel.PDDocumentCatalog.getPages()
	// PDPageNode org.apache.pdfbox.pdmodel.PDDocumentCatalog.getPages()

	
	
	public ObjectInvestigator(File f) {
		
			try {
				PDDocument pdfDocument = PDDocument.load(f);
				System.out.println(pdfDocument.getVersion());
				investigate(pdfDocument);
			
				
				printOutTheDocumentDictionary(pdfDocument);
				
						
//				PDPageTree pagesFromCatalog = catalog.getPages();
//				System.out.println("pagesFromCatalog size = " + pagesFromCatalog.spliterator().getExactSizeIfKnown());
				System.out.println(pdfDocument.getVersion());
				pdfDocument.close();
			} catch (InvalidPasswordException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	private void printOutTheDocumentDictionary(PDDocument pdfDocument) {
		PDDocumentCatalog catalog = pdfDocument.getDocumentCatalog();
		COSDictionary theDocumentDictionary = catalog.getCOSObject();
		System.out.println("**************");
		System.out.println("the Document Dictionary");
		System.out.println("***************" );
		for (Map.Entry<COSName, COSBase> es : theDocumentDictionary.entrySet()) {
			System.out.println("key:" + es.getKey() + " value:" + es.getValue()); 
			}
	}

	private void investigate(PDDocument pdfDocument) {
		PDPageTree allPages  = pdfDocument.getDocumentCatalog().getPages();
		System.out.println(allPages.getCount() + " pages");
		int pageCount = 0;
		for (PDPage p:allPages) {
			investigateThisPage(p, pageCount++);
		}
	}

	private void investigateThisPage(PDPage p, int pageCount) {
		COSDictionary dictionaryForThisPage = p.getCOSObject();		
		 int x = 0;
		 int y = pageCount;
		 
		
		 x = findSeveralOtherPageAccessors(p, pageCount, x, y);
		 
		for (COSName cd:dictionaryForThisPage.keySet()) {			
           
			Debugwindow debDictionary = new Debugwindow("Dicitionary key " + cd.getName() + " page " + pageCount, x++, y);
			COSBase theDictionaryObject = dictionaryForThisPage.getDictionaryObject(cd);
			try {
				theDictionaryObject.accept(new Visitor(debDictionary));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            debDictionary.primaryWriteln(theDictionaryObject.getClass().getName());
            decompose(debDictionary, theDictionaryObject);

            //  debDictionary.newLine(pageCount);
			System.out.println("-------------- " + cd.getName() + " ----------------------");
			System.out.println(theDictionaryObject);
			System.out.println();
		}
	}

	private int findSeveralOtherPageAccessors(PDPage p, int pageCount, int x, int y) {
		try {
            Debugwindow dw = new Debugwindow("Page contents  page " + pageCount, x++, y);
            dw.primaryWriteln("Size of page dictionary:" + p.getCOSObject().size());
			InputStream contents = p.getContents();			
			dw.primaryWriteln("Contents inp stream available " + contents.available());
			dw.primaryWriteln("Contents inp stream is the same as in Dictionary key Contents");
			
			InputStreamReader isr = new InputStreamReader(contents);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int n;
			while ((n = isr.read()) != -1) {		
				bos.write(n);
				}
			bos.close();
			dw.hex(bos.toByteArray());
			dw.paneBold(new String(bos.toByteArray()));
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return x;
	}

	private void decompose(Debugwindow debDictionary, COSBase cosBase) {
		debDictionary.paneSmall("isDirect:" + cosBase.isDirect());
		debDictionary.paneBold(cosBase.getClass().getName());
		debDictionary.paneBlue(cosBase.toString());
		
		
		// Let a class be indexed from an arraylist to enter the correct method
		switch(CLASSES.indexOf(cosBase.getClass()))  {
		case 0 : investigateCOSArray(debDictionary, cosBase); break;
		case 1 : investigateCOSBoolean(debDictionary, cosBase); break;
		case 2 : investigateCOSDictionary(debDictionary, cosBase); break;
		case 3 : investigateCOSDocument(debDictionary, cosBase); break;
		case 4 : investigateCOSName(debDictionary, cosBase); break;
		case 5 : investigateCOSNull(debDictionary, cosBase); break;		
		case 6 : investigateCOSNumber(debDictionary, cosBase); break;
		case 7 : investigateCOSObject(debDictionary, cosBase); break;
		case 8 : investigateCOSString(debDictionary, cosBase); break;
		case 9 : investigateCOSStream(debDictionary, cosBase); break;
		};
	}

	private void investigateCOSStream(Debugwindow debDictionary, COSBase cosBase) {
		COSStream o = (COSStream) cosBase;
		debDictionary.paneBold("size of cosBase:"  + o.size());
		for (Map.Entry<COSName, COSBase> es : o.entrySet()) {
			debDictionary.paneBlue("key:" + es.getKey() + " value:" + es.getValue()); 
			}
		
	}

	private void investigateCOSString(Debugwindow debDictionary, COSBase cosBase) {
		COSString o = (COSString) cosBase;
		
	}

	private void investigateCOSObject(Debugwindow debDictionary, COSBase cosBase) {
		COSObject o = (COSObject) cosBase;		
	}

	private void investigateCOSNumber(Debugwindow debDictionary, COSBase cosBase) {
		COSNumber o = (COSNumber) cosBase;
	}

	private void investigateCOSName(Debugwindow debDictionary, COSBase cosBase) {
		COSName o = (COSName) cosBase;		
	}

	private void investigateCOSNull(Debugwindow debDictionary, COSBase cosBase) {
		COSNull o = (COSNull) cosBase;
	}

	private void investigateCOSDocument(Debugwindow debDictionary, COSBase cosBase) {
		COSDocument o = (COSDocument) cosBase;
	}

	private void investigateCOSDictionary(Debugwindow debDictionary, COSBase cosBase) {
		COSDictionary o = (COSDictionary) cosBase;
		debDictionary.paneBold("size of dictionary:"  + o.size());
	}

	private void investigateCOSBoolean(Debugwindow debDictionary, COSBase cosBase) {
		COSBoolean o = (COSBoolean) cosBase;
		
	}

	private void investigateCOSArray(Debugwindow debDictionary, COSBase cosBase) {
		COSArray o = (COSArray) cosBase;
		debDictionary.paneBold("size of array:"  + o.size());
	}

}

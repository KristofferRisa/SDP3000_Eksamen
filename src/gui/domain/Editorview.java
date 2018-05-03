package gui.domain;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.text.PDFTextStripper;

import controller.Controller;
import logic.Visitor;

public class Editorview extends JPanel implements DocumentListener {
		
	JTextArea text = new JTextArea();
	File file = new File (".");
	JFileChooser jf = new JFileChooser(file);
	private PDDocument doc;
	
	public Editorview() {
		setLayout(new BorderLayout());
		add(new JScrollPane(text));
		text.getDocument().addDocumentListener(this);
	}
	
	public void save() {
		jf.setApproveButtonText("Lagre tekstfil");
		jf.setCurrentDirectory(file);
		jf.showSaveDialog(null);		
		if ((file = jf.getSelectedFile())!=null) {
			saveTo(file);
		}
	}
	
	private void saveTo(File f) {			
		try {
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(text.getText().getBytes());
			fo.close();
		} catch (Exception e) {}				
	}

	public void load() {		
		jf.setApproveButtonText("Velg tekstfil");
		jf.setCurrentDirectory(file);
		jf.showOpenDialog(null);
		if ((file = jf.getSelectedFile())!=null) {			
			text.setText(loadFrom(file));
		}
	}
	
	public String getHtml() {
		return text.getText();
	}
	
	private String loadFrom(File f) {
		String content = "";
		try {
			System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
			
			doc = PDDocument.load(f);
            //content = new PDFTextStripper().getText(doc);
			
			Visitor visitor = new Visitor();
	        
	        PDDocument pdfDocument = PDDocument.load(file);
	        
	        PDPageTree allPages  = pdfDocument.getDocumentCatalog().getPages();
			System.out.println(allPages.getCount() + " pages");
	        
	        for (PDPage p:allPages) {
	        	COSDictionary dictionaryForThisPage = p.getCOSObject();
	            for (COSName cd:dictionaryForThisPage.keySet()) {			
	    	        try {
	    	        	
	    	        	COSBase theDictionaryObject = dictionaryForThisPage.getDictionaryObject(cd);
	    	        	theDictionaryObject.accept(visitor);
	    				
	    			} catch (IOException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    			
	            }
			}
			
			
			for (int i = 0; i < doc.getNumberOfPages(); ++i)
			{
			    PDPage page = doc.getPage(i);
			    PDResources res = page.getResources();
			    for (COSName fontName : res.getFontNames())
			    {
			        PDFont font = res.getFont(fontName);
			        // do stuff with the font
			        System.out.println("Font: " + font.getFontDescriptor().getFontName());
			    }
			}
			
            content = new PDFText2HTML().getText(doc);
                

		}
		catch (IOException e) {
            System.out.println(e.getMessage());
		}
		return content;
	}

	public void paste() {		
		text.paste();		
	}
	
	public void insertUpdate(DocumentEvent arg0) {
		Controller.testhtml();		
	}

	public void removeUpdate(DocumentEvent arg0) {
		Controller.testhtml();
		
	}

	public void changedUpdate(DocumentEvent arg0) {
		Controller.testhtml();		
	}
		
}

package gui.domain;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;


import controller.Controller;

public class Editorview extends JPanel implements DocumentListener {
		
	JTextArea text = new JTextArea();
	
	File file = new File (".");
	
	JFileChooser jf = new JFileChooser(file);
	
	public Editorview() {
		
		FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("PDF Files", "pdf");
		jf.setFileFilter(pdfFilter);
		
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
		
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

		String htmlFilnavn = f.getName().replaceAll(".pdf", ".html");
		try {
			
			ConvertPdfToHtmlFile htmlFile = new ConvertPdfToHtmlFile(htmlFilnavn, 1, 1);
			htmlFile.convertPdfToHtml(f.getName());
			htmlFile.closeFile();
			
		} catch (Exception e) {
		    System.err.println( "Filed to convert Pdf to Html." );
			e.printStackTrace();
		}
		
		String content = "";
		if(f.exists() && !f.isDirectory()) { 
			try {
		        BufferedReader in = new BufferedReader(new FileReader(htmlFilnavn));
		        String str;
		        while ((str = in.readLine()) != null) {
		            content +=str;
		        }
		        in.close();
		    	} catch (IOException e) {
		    }
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

	public void se() {
		if(file != null)
		{
			try {
				Desktop.getDesktop().browse(file.toURI());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
}

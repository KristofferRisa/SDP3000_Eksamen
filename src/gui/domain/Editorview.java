package gui.domain;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.Controller;

public class Editorview extends JPanel implements DocumentListener {
		
	private static final long serialVersionUID = 1398799947820949273L;

	JTextArea text = new JTextArea();
	
	File file = new File (".");
	
	JFileChooser jf = new JFileChooser(file);

	boolean isConverted;

	private PdfToHtmlConverter converter;
	
	public Editorview() {
		
		setLayout(new BorderLayout());
		add(new JScrollPane(text));
		
		text.getDocument().addDocumentListener(this);
	}
	
	public void save() {
		FileNameExtensionFilter htmlFilter = new FileNameExtensionFilter("HTML Files", "html", "htm", "xhtml");

		jf.setSelectedFile(new File(file.getName().replaceAll(".pdf", ".html")));
		
		jf.setFileFilter(htmlFilter);
		jf.setApproveButtonText("Eksporter HTML");
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
			isConverted = true;
			
		} catch (Exception e) {}				
	}

	public void load() {
		
		isConverted = false;
		
		FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("PDF Files", "pdf");
		
		jf.setFileFilter(pdfFilter);
		jf.setApproveButtonText("Velg tekstfil");
		jf.setCurrentDirectory(file);
		jf.showOpenDialog(null);
		
		if ((file = jf.getSelectedFile())!=null) {			
			text.setText(loadFrom(file));
			isConverted = false;
		}
	}
	
	public String getHtml() {
		return text.getText();
	}
	
	private String loadFrom(File f) {
		
		System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

		try 
		{
			converter = new PdfToHtmlConverter(f);
			converter.convertPdfToHtml();
			
			return converter.getText();
			
		} catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
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
		if(!isConverted) {
			int dialogResult = JOptionPane.showConfirmDialog (
					null
					, "HTML koden er ikke eksportert, ønsker du å eksportere nå?"
					,"Question"
					,JOptionPane.YES_NO_CANCEL_OPTION);
			if(dialogResult == JOptionPane.YES_OPTION){
			  Controller.save();
			}
		} 
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

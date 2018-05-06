package gui.domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.util.PDFImageWriter;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

public class PdfToHtmlConverter extends PDFTextStripper {
	
	private StringBuilder html;
	
	private File file;
	
	private int toppMargBilde = 0;

	private int resolution = 72;

	private List<String> images = new ArrayList<String>();
	
	public PdfToHtmlConverter(File f) throws IOException
	{
		// Bygger opp HTML og legger til referanse mot PDF filen
		file = f;
		html = new StringBuilder();
		html.append("<html>" +
				"<head> \r\n" +
				"<title>Html file</title>" +
				"<link rel=\"stylesheet\" href=\"css/style.css\" />" +
				"</head>" +
				"<body>");
	}
	
	/**
	 * Metode for å hente den konvertere HTML koden
	 * @return HTML kode
	 */
	public String getText() 
	{
		return html.toString();
	}
	
	/**
	 * Metode for å konvertere PDF til HTML
	 * @throws IOException
	 */
	public void convertPdfToHtml() throws IOException 
	{	
		PDDocument doc = null;
        try 
        {
        	doc = PDDocument.load(file);
        	List<PDPage> alleSider = doc.getDocumentCatalog().getAllPages();
            
            for( int i=0; i<alleSider.size(); i++ ) 
            {
            	PDPage pdSide = alleSider.get(i);
            	BufferedImage img = pdSide.convertToImage(BufferedImage.TYPE_INT_RGB, resolution);
            	
            	System.out.print("Behandler side " + i);

                images.add(file.getName() +(int)(i+1) + ".png");
                
            	// Legger til CSS bakgrunn i DIV klasse
        		html.append( 
        				"<div class=\"background\" style=\"position: absolute; width: " + img.getWidth()
        				+ "; height: " + img.getHeight() 
        				+ "; background: url('file:" +  file.getName() +(int)(i+1) + ".png') "
        				+ "top left no-repeat; margin-top: "+ toppMargBilde 
        				+ "\">");
        		
        		toppMargBilde += img.getHeight();
            	PDStream contents = pdSide.getContents();
            	
            	if( contents != null ) 
            	{
            		this.processStream(
            				pdSide
            				, pdSide.findResources()
            				, pdSide.getContents().getStream()
            				);
            	}

            	html.append("</span> \n");
            	html.append( "</div> \n");
            }
             
            // Parser pr side og fjerner text fra bakgrunnsbilde
            for( int i=0; i<alleSider.size(); i++ ) 
            {
            	PDPage side = alleSider.get( i );
                
            	PDFStreamParser behandler = new PDFStreamParser(side.getContents());
                behandler.parse();
                
                List tokens = behandler.getTokens();
                List nyeTokens = new ArrayList();
                
                for( int j=0; j<tokens.size(); j++)
                {
                    Object token = tokens.get( j );
                    if( token instanceof PDFOperator )
                    {
                        PDFOperator op = (PDFOperator)token;
                        if( op.getOperation().equals( "TJ") || op.getOperation().equals( "Tj" ))
                        {
                            nyeTokens.remove( nyeTokens.size() -1 );
                            continue;
                        }
                    }
                    nyeTokens.add(token);
                }
                
                PDStream nyttInnhold = new PDStream(doc);
                
                ContentStreamWriter skriver = new ContentStreamWriter( nyttInnhold.createOutputStream() );
                skriver.writeTokens(nyeTokens);
                side.setContents(nyttInnhold);
            }
            
            // Skriver bilder til disk
            PDFImageWriter imageWriter = new PDFImageWriter();
            
            boolean success = imageWriter.writeImage(
            		doc
            		, "png"
            		, ""
            		,1
            		, Integer.MAX_VALUE
            		, file.getName()
            		, BufferedImage.TYPE_INT_RGB
            		, (int) (resolution));
            
            if (!success)
            {
                System.err.println("Feilet ved skriving av image");
            }
            
            // Fullfører HTML kode
        	html.append("</body> \n</html>");
            
        }
        finally 
        {
            if( doc != null ) 
            {
                doc.close();
            }
        }
	}
	
    protected void processTextPosition(TextPosition text)
    {
		int fontStr = Math.round(text.getFontSizeInPt() );
		int margTopp = (int)((text.getYDirAdj()) - fontStr);
   		int margVenstre = (int)((text.getXDirAdj()));

   		PDFont font = text.getFont();
		PDFontDescriptor fontDescriptor = font.getFontDescriptor();
		
		String fontString = "";
		if (fontDescriptor != null) {
    		fontString = fontDescriptor.getFontName();    			
		}
		else {
			fontString = "";	
		}

		int indexPlus = fontString.indexOf("+");
		if (indexPlus != -1) {
			fontString = fontString.substring(indexPlus+1);
		}
		boolean isBold = fontString.contains("Bold");
		boolean isItalic = fontString.contains("Italic");

		int indexDash = fontString.indexOf("-");
		if (indexDash != -1) {
			fontString = fontString.substring(0, indexDash);
		}
		int indexComa = fontString.indexOf(",");
		if (indexComa != -1) {
			fontString = fontString.substring(0, indexComa);
		}
	
    	html.append(
				"<span style=\"position: absolute; margin-left:"+margVenstre
				+ "px; margin-top: " + margTopp + "px; "
				+ "font-size: "+fontStr + "px; "
				+ "font-family:"+fontString+";");
		if (isBold) {
			html.append("font-weight: bold;");
		}
		if (isItalic) {
			html.append("font-style: italic;");
		}
		html.append("\">\n");
	
		html.append(text.getCharacter());

		html.append("</span>\n"); 		
    }	
    
	public List<String> getImages() {
		return images;
	}
    
}
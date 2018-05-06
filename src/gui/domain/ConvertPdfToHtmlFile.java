package gui.domain;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

public class ConvertPdfToHtmlFile extends PDFTextStripper{
	
	private BufferedWriter htmlFile;
	
	private int marginTopBackground = 0;

	float previousAveCharWidth = -1;
    
	private int resolution = 72; 
	
   //Konverterer pdf til HTML
    public ConvertPdfToHtmlFile(File file) throws IOException{
    		htmlFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
			String header =
					"<html>" +
					"<head> \r\n" +
					"<title>Html file</title>" +
					"<link rel=\"stylesheet\" href=\"css/style.css\" />" +
					"</head>" +
					"<body>";
			htmlFile.write(header);
	}
	
    /**
     * Lukker HTML filen
     */
	public void closeFile() {
		try {
			String footer = "</body>"
					+"</html>";
			htmlFile.write(footer);
			htmlFile.close();
		} catch (IOException e) {
			e.printStackTrace();
            System.err.println( "Error: IO error, could not close html file." );
		}
	}
	

	
    /**
     * Convert a PDF file to HTML
     *
     * @param pathToPdf Path to the PDF file
     *
     * @throws IOException If there is an error processing the operation.
     */
	public void convertPdfToHtml(String pathToPdf) throws Exception {
		int positionDotPdf = pathToPdf.lastIndexOf(".pdf");
		if (positionDotPdf == -1) {
            System.err.println("File doesn't have .pdf extension");
		}
		int positionLastSlash = pathToPdf.lastIndexOf("/");
		if (positionLastSlash  == -1) {
			positionLastSlash  = 0;
		}
		else {
			positionLastSlash++;
		}
		String fileName = pathToPdf.substring(positionLastSlash, positionDotPdf);
		
		
		PDDocument document = null;
        try {
        	document = PDDocument.load(pathToPdf);

            List allPages = document.getDocumentCatalog().getAllPages();
            
            // Retrieve and save text in the HTML file
            for( int i=0; i<allPages.size(); i++ ) {
            	System.out.println("Processing page "+i);
            	PDPage page = (PDPage)allPages.get(i);

            	BufferedImage image = page.convertToImage(BufferedImage.TYPE_INT_RGB, resolution);
            	
        		htmlFile.write( 
        				"<div class=\"background\" style=\"position: absolute; width: " + image.getWidth()
        				+ "; height: " + image.getHeight() 
        				+ "; background: url('file:" +  fileName+(int)(i+1)+".png') "
        				+ "top left no-repeat; margin-top: "+ marginTopBackground 
        				+ "\">");
        		htmlFile.newLine();
        		marginTopBackground += image.getHeight();
            	PDStream contents = page.getContents();
            	if( contents != null ) {
            		this.processStream( page, page.findResources(), page.getContents().getStream() );
            	}

            	htmlFile.write("</span>");
            	htmlFile.newLine();
            	htmlFile.write( "</div>");
            	htmlFile.newLine();
            	
            }
            
            // Remove the text
            for( int i=0; i<allPages.size(); i++ ) {
                PDPage page = (PDPage)allPages.get( i );
                PDFStreamParser parser = new PDFStreamParser(page.getContents());
                parser.parse();
                List<Object> tokens = parser.getTokens();
                 ArrayList<Object> newTokens = new ArrayList<Object>();
                for( int j=0; j<tokens.size(); j++)
                {
                    Object token = tokens.get( j );
                    if( token instanceof PDFOperator )
                    {
                        PDFOperator op = (PDFOperator)token;
                        if( op.getOperation().equals( "TJ") 
                        		|| op.getOperation().equals( "Tj" ))
                        {
                            newTokens.remove( newTokens.size() -1 );
                            continue;
                        }
                    }
                    newTokens.add( token );
                }
                PDStream newContents = new PDStream( document );
                ContentStreamWriter writer = new ContentStreamWriter( newContents.createOutputStream() );
                writer.writeTokens( newTokens );
                page.setContents( newContents );
            }
            PDFImageWriter imageWriter = new PDFImageWriter();
            
            String imageFormat = "png";
            int startPage = 1;
            int endPage = Integer.MAX_VALUE;
            String outputPrefix = pathToPdf.substring(0, positionLastSlash)+fileName;
            int imageType = BufferedImage.TYPE_INT_RGB;
            

            boolean success = imageWriter.writeImage(
            		document
            		, imageFormat
            		, ""
            		,startPage
            		, endPage
            		, outputPrefix
            		, imageType
            		, (int) (resolution));
            if (!success)
            {
                System.err.println( "Error: no writer found for image format '"
                        + imageFormat + "'" );
            }
            
        }
        finally {
            if( document != null ) {
                document.close();
            }
        }
	}
	
    protected void processTextPosition( TextPosition text )
    {
    	try {
    		
       		int marginLeft = (int)((text.getXDirAdj()));
    		int fontSizePx = Math.round(text.getFontSizeInPt()/ 72 * resolution );
    		int marginTop = (int)((text.getYDirAdj()) - fontSizePx);


    		String fontString = "";
    		PDFont font = text.getFont();
    		PDFontDescriptor fontDescriptor = font.getFontDescriptor();
    		
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
    	
        	renderingSimple(text, marginLeft,  marginTop, fontSizePx, fontString, isBold, isItalic);
            
//    				renderingSimple(text, marginLeft,  marginTop, fontSizePx, fontString, isBold, isItalic); //0
//    				renderingGroupByWord(text, marginLeft,  marginTop, fontSizePx, fontString, isBold, isItalic); // 1
//    				renderingGroupByLineNoCache(text, marginLeft,  marginTop, fontSizePx, fontString, isBold, isItalic); //2
//    				renderingGroupByLineWithCache(text, marginLeft,  marginTop, fontSizePx, fontString, isBold, isItalic); //3
    	
    	    
		} catch (IOException e) {
			e.printStackTrace();
		}
    }	
    
    
    private void renderingSimple(TextPosition text, int marginLeft, int marginTop, int fontSizePx, String fontString, boolean isBold, boolean isItalic) throws IOException {
		htmlFile.write("<span style=\"position: absolute; margin-left:"+marginLeft+"px; margin-top: "+marginTop+"px; font-size: "+fontSizePx+"px; font-family:"+fontString+";");
		if (isBold) {
			htmlFile.write("font-weight: bold;");
		}
		if (isItalic) {
			htmlFile.write("font-style: italic;");
		}
		htmlFile.write("\">");
		htmlFile.newLine();
	
		htmlFile.write(text.getCharacter());

		htmlFile.write("</span>"); 
		htmlFile.newLine();
    }
    
}
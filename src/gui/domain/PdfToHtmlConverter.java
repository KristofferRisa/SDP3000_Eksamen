//package gui.domain;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
//import org.apache.pdfbox.text.PDFTextStripper;
//import org.apache.pdfbox.text.TextPosition;
//
//
//public class PdfToHtmlConverter extends  PDFTextStripper
//{
//
//	private static final int INITIAL_PDF_TO_HTML_BYTES = 8192;
//	
//	private final FontState fontState = new FontState();
//
//	public PdfToHtmlConverter() throws IOException {
//		super();
//		
//		setLineSeparator(LINE_SEPARATOR);
//        
//		setParagraphStart("<p>");
//        setParagraphEnd("</p>"+ LINE_SEPARATOR);
//        
//        setPageStart("<div style=\"page-break-before:always; page-break-after:always\">");
//        setPageEnd("</div>"+ LINE_SEPARATOR);
//        
//        setArticleStart(LINE_SEPARATOR);
//        setArticleEnd(LINE_SEPARATOR);
//	}
//	
//	@Override
//	protected void startDocument(PDDocument doc) throws IOException
//	{
//		StringBuilder buf = new StringBuilder(INITIAL_PDF_TO_HTML_BYTES);
//      
//		buf.append("<html>\n");
//		buf.append("<head>\n");
//		buf.append("<title>").append(escape(getTitle())).append("</title>\n");
//		//      buf.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"UTF-8\">\n");
//		buf.append("</head>\n");
//		buf.append("<body>\n");      
//		  
//		super.writeString(buf.toString());
//	}
//
//    @Override
//    public void endDocument(PDDocument document) throws IOException
//    {
//        super.writeString("</body></html>");
//    }
//    
//    protected String getTitle()
//    {
//        String titleGuess = document.getDocumentInformation().getTitle();
//        if(titleGuess != null && titleGuess.length() > 0)
//        {
//            return titleGuess;
//        }
//        else
//        {
//            Iterator<List<TextPosition>> textIter = getCharactersByArticle().iterator();
//            float lastFontSize = -1.0f;
//
//            StringBuilder titleText = new StringBuilder();
//            while (textIter.hasNext())
//            {
//                for (TextPosition position : textIter.next())
//                {
//                    float currentFontSize = position.getFontSize();
//                    //If we're past 64 chars we will assume that we're past the title
//                    //64 is arbitrary
//                    if (currentFontSize != lastFontSize || titleText.length() > 64)
//                    {
//                        if (titleText.length() > 0)
//                        {
//                            return titleText.toString();
//                        }
//                        lastFontSize = currentFontSize;
//                    }
//                    if (currentFontSize > 13.0f)
//                    { // most body text is 12pt
//                        titleText.append(position.getUnicode());
//                    }
//                }
//            }
//        }
//        return "";
//    }
//    
//    @Override
//    protected void endArticle() throws IOException
//    {
//        super.endArticle();
//        super.writeString("</div>");
//    }
//
//    @Override
//    protected void writeString(String text, List<TextPosition> textPositions) throws IOException
//    {
//        super.writeString(fontState.push(text, textPositions));
//    }
//    
//    @Override
//    protected void writeString(String chars) throws IOException
//    {
//        super.writeString(escape(chars));
//    }
//    
//    @Override
//    protected void writeParagraphEnd() throws IOException
//    {
//        // do not escape HTML
//        super.writeString(fontState.clear());
//        
//        super.writeParagraphEnd();
//    }
//    
//    private static String escape(String chars)
//    {
//        StringBuilder builder = new StringBuilder(chars.length());
//        for (int i = 0; i < chars.length(); i++)
//        {
//            appendEscaped(builder, chars.charAt(i));
//        }
//        return builder.toString();
//    }
//    
//    private static void appendEscaped(StringBuilder builder, char character)
//    {
//        // write non-ASCII as named entities
//        if ((character < 32) || (character > 126))
//        {
//            int charAsInt = character;
//            builder.append("&#").append(charAsInt).append(";");
//        }
//        else
//        {
//            switch (character)
//            {
//            case 34:
//                builder.append("&quot;");
//                break;
//            case 38:
//                builder.append("&amp;");
//                break;
//            case 60:
//                builder.append("&lt;");
//                break;
//            case 62:
//                builder.append("&gt;");
//                break;
//            default:
//                builder.append(String.valueOf(character));
//            }
//        }
//    }
//    
//    private static class FontState
//    {
//        private final List<String> stateList = new ArrayList<String>();
//        private final Set<String> stateSet = new HashSet<String>();
//
//        /**
//         * Pushes new {@link TextPosition TextPositions} into the font state. The state is only
//         * preserved correctly for each letter if the number of letters in <code>text</code> matches
//         * the number of {@link TextPosition} objects. Otherwise, it's done once for the complete
//         * array (just by looking at its first entry).
//         *
//         * @return A string that contains the text including tag changes caused by its font state.
//         */
//        public String push(String text, List<TextPosition> textPositions)
//        {
//            StringBuilder buffer = new StringBuilder();
//
//            if (text.length() == textPositions.size())
//            {
//                // There is a 1:1 mapping, and we can use the TextPositions directly
//                for (int i = 0; i < text.length(); i++)
//                {
//                    push(buffer, text.charAt(i), textPositions.get(i));
//                }
//            }
//            else if (!text.isEmpty())
//            {
//                // The normalized text does not match the number of TextPositions, so we'll just
//                // have a look at its first entry.
//                // TODO change PDFTextStripper.normalize() such that it maintains the 1:1 relation
//                if (textPositions.isEmpty())
//                {
//                    return text;
//                }
//                push(buffer, text.charAt(0), textPositions.get(0));
//                buffer.append(escape(text.substring(1)));
//            }
//            return buffer.toString();
//        }
//
//        /**
//         * Closes all open states.
//         * @return A string that contains the closing tags of all currently open states.
//         */
//        public String clear()
//        {
//            StringBuilder buffer = new StringBuilder();
//            closeUntil(buffer, null);
//            stateList.clear();
//            stateSet.clear();
//            return buffer.toString();
//        }
//
//        protected String push(StringBuilder buffer, char character, TextPosition textPosition)
//        {
//            boolean bold = false;
//            boolean italics = false;
//
//            PDFontDescriptor descriptor = textPosition.getFont().getFontDescriptor();
//            if (descriptor != null)
//            {
//                bold = isBold(descriptor);
//                italics = isItalic(descriptor);
//            }
//            
//            buffer.append(bold ? open("b") : close("b"));
//            buffer.append(italics ? open("i") : close("i"));
//            appendEscaped(buffer, character);
//
//            return buffer.toString();
//        }
//
//        private String open(String tag)
//        {
//            if (stateSet.contains(tag))
//            {
//                return "";
//            }
//            stateList.add(tag);
//            stateSet.add(tag);
//
//            return openTag(tag);
//        }
//
//        private String close(String tag)
//        {
//            if (!stateSet.contains(tag))
//            {
//                return "";
//            }
//            // Close all tags until (but including) the one we should close
//            StringBuilder tagsBuilder = new StringBuilder();
//            int index = closeUntil(tagsBuilder, tag);
//
//            // Remove from state
//            stateList.remove(index);
//            stateSet.remove(tag);
//
//            // Now open the states that were closed but should remain open again
//            for (; index < stateList.size(); index++)
//            {
//                tagsBuilder.append(openTag(stateList.get(index)));
//            }
//            return tagsBuilder.toString();
//        }
//
//        private int closeUntil(StringBuilder tagsBuilder, String endTag)
//        {
//            for (int i = stateList.size(); i-- > 0;)
//            {
//                String tag = stateList.get(i);
//                tagsBuilder.append(closeTag(tag));
//                if (endTag != null && tag.equals(endTag))
//                {
//                    return i;
//                }
//            }
//            return -1;
//        }
//
//        private String openTag(String tag)
//        {
//            return "<" + tag + ">";
//        }
//
//        private String closeTag(String tag)
//        {
//            return "</" + tag + ">";
//        }
//
//        private boolean isBold(PDFontDescriptor descriptor)
//        {
//            if (descriptor.isForceBold())
//            {
//                return true;
//            }
//            return descriptor.getFontName().contains("Bold");
//        }
//
//        private boolean isItalic(PDFontDescriptor descriptor)
//        {
//            if (descriptor.isItalic())
//            {
//                return true;
//            }
//            return descriptor.getFontName().contains("Italic");
//        }
//    }
//
//}

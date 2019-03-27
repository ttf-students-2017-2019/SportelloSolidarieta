package utilities;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import model.Meeting;

public class PdfUtil {

	private static PDDocument document;
	private static PDPageContentStream contentStream;
	private static PDFont font = PDType1Font.COURIER;
	private static float tx = 56.7f;
	private static float ty = 785.2f;
	private static float fontSize = 10;
	private static float leading = 14.5f;
	private static int charsPerLine = 80;
	private static int linesPerPage = 52;
	private static int currentPage;
	private static int currentLine;
	
	public static void export(List<Meeting> meetings, LocalDate from, LocalDate to, String total, String path) {
		document = new PDDocument();
		newPage(from, to);
	    for (Meeting m : meetings) {
	    	if (currentLine > linesPerPage - 2) {
	    		newPage(true);
	    	} 
	    	writeRow(m);
	    }
	    closeDocument(total, path);
	}
	
	private static void newPage(LocalDate from, LocalDate to) {
		try {
			currentLine = 1;
			PDPage page = new PDPage(PDRectangle.A4);
		    document.addPage(page);
			contentStream = new PDPageContentStream(document, page);
	    	contentStream.beginText();
	    	contentStream.newLineAtOffset(tx, ty);
	    	contentStream.setFont(font, fontSize);
	    	contentStream.setLeading(leading);
    		writeHeading(from, to);
	    	writeHeader();
			currentPage++;
	    } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void newPage(boolean headerRequired) {
		try {
    		writeFooter();
			contentStream.endText();
			contentStream.close();
			currentLine = 1;
			PDPage page = new PDPage(PDRectangle.A4);
		    document.addPage(page);
			contentStream = new PDPageContentStream(document, page);
	    	contentStream.beginText();
	    	contentStream.newLineAtOffset(tx, ty);
	    	contentStream.setFont(font, fontSize);
	    	contentStream.setLeading(leading);
	    	if (headerRequired) {
	    	writeHeader();
	    	}
			currentPage++;
	    } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeHeading(LocalDate from, LocalDate to) {
		try {
			contentStream.showText("Report incontri dal " + Formatter.formatDate(from) + " al " + Formatter.formatDate(to));
			contentStream.newLine();
			contentStream.newLine();
			currentLine += 2;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void writeHeader() {
		try {
			contentStream.showText("COGNOME" + StringUtils.repeat(" ", (charsPerLine - 20) / 2 - 7) + "NOME" + StringUtils.repeat(" ", (charsPerLine - 20) / 2 - 4) + "DATA" + StringUtils.repeat(" ", 9) + "IMPORTO");
	    	contentStream.newLine();
	    	contentStream.showText(StringUtils.repeat("-", charsPerLine));
	    	contentStream.newLine();
			currentLine += 2;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeRow(Meeting m) {
		try {
			contentStream.showText(m.getPersonSurname() + StringUtils.repeat(" ", (charsPerLine - 20) / 2 - m.getPersonSurname().length()) + m.getPersonName() + StringUtils.repeat(" ",  (charsPerLine - 20) / 2 - m.getPersonName().length()) + Formatter.formatDate(m.getDate()) + StringUtils.repeat(" ", 10 - Formatter.formatNumber(m.getAmount()).length()) + Formatter.formatNumber(m.getAmount()));
			contentStream.newLine();
			currentLine++;
    	} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void writeTotal(String total) {
		try {
			if (currentLine < linesPerPage - 2) {
				contentStream.newLine();
				currentLine++;
			} else {
				newPage(false);
			}
			contentStream.showText(StringUtils.repeat(" ", charsPerLine - 9 - total.length()) + "TOTALE = " + total);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private static void writeFooter() {
		try {
			for (int i = 0; i < linesPerPage - currentLine; i++) {
				contentStream.newLine();
			}
			contentStream.showText(StringUtils.repeat(" ", charsPerLine - 7 - String.valueOf(currentPage).length()) + "Pagina " + currentPage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	private static void closeDocument(String total, String path) {
		try {
			writeTotal(total);
			writeFooter();
			contentStream.endText();
			contentStream.close();
			document.save(path);
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

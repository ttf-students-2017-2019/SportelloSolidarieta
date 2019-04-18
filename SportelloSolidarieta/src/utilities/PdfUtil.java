package utilities;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import report.ObservableMeeting;

public class PdfUtil {
	
	public enum ReportType {
		OutgoingsOnly,
		IncomesOnly,
		OutgoingsAndIncomes
	}

	private static ReportType reportType;
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
	
	public static void export(ReportType requestedReportType, List<ObservableMeeting> meetings, LocalDate from, LocalDate to, String totalOutgoings, String totalIncomes, String balanceValue, String path) {
		reportType = requestedReportType;
		document = new PDDocument();
		newPage(from, to);
	    for (ObservableMeeting m : meetings) {
	    	if (currentLine > linesPerPage - 2) {
	    		newPage(true);
	    	} 
	    	writeRow(m);
	    }
	    closeDocument(totalOutgoings, totalIncomes, balanceValue, path);
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
    		writePageHeader(from, to);
	    	writeTableHeader();
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
	    	writeTableHeader();
	    	}
			currentPage++;
	    } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writePageHeader(LocalDate from, LocalDate to) {
		try {
			contentStream.showText("Report dal " + Formatter.formatDate(from) + " al " + Formatter.formatDate(to));
			contentStream.newLine();
			contentStream.newLine();
			currentLine += 2;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void writeTableHeader() {
		try {
			contentStream.showText("COGNOME" + StringUtils.repeat(" ", (charsPerLine - 30) / 2 - 7) + "NOME" + StringUtils.repeat(" ", (charsPerLine - 30) / 2 - 4) + "DATA" + StringUtils.repeat(" ", 10) + "USCITE" + StringUtils.repeat(" ", 3) + "ENTRATE");
	    	contentStream.newLine();
	    	contentStream.showText(StringUtils.repeat("-", charsPerLine));
	    	contentStream.newLine();
			currentLine += 2;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeRow(ObservableMeeting m) {
		try {
			contentStream.showText(m.getAssistedSurname().get() + StringUtils.repeat(" ", (charsPerLine - 30) / 2 - m.getAssistedSurname().get().length()) + m.getAssistedName().get() + StringUtils.repeat(" ",  (charsPerLine - 30) / 2 - m.getAssistedName().get().length()) + Formatter.formatDate(m.getDate()) + StringUtils.repeat(" ", 10 - m.getOutgoings().get().length()) + m.getOutgoings().get() + StringUtils.repeat(" ", 10 - m.getIncomes().get().length()) + m.getIncomes().get());
			contentStream.newLine();
			currentLine++;
    	} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private static void writeTotal(String totalOutgoings, String totalIncomes, String balanceValue) {
		switch (reportType) {
		case OutgoingsOnly:
			try {
				if (currentLine < linesPerPage - 2) {
					contentStream.newLine();
					currentLine++;
				} else {
					newPage(false);
				}
				contentStream.showText(StringUtils.repeat(" ", charsPerLine - 16 - totalOutgoings.length()) + "TOTALE USCITE = " + totalOutgoings);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case IncomesOnly:
			try {
				if (currentLine < linesPerPage - 2) {
					contentStream.newLine();
					currentLine++;
				} else {
					newPage(false);
				}
				contentStream.showText(StringUtils.repeat(" ", charsPerLine - 17 - totalIncomes.length()) + "TOTALE ENTRATE = " + totalIncomes);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			
		case OutgoingsAndIncomes:
			// for proper tabulation
			List<Integer> valueLengths = new ArrayList<Integer>();
			valueLengths.add(totalOutgoings.length());
			valueLengths.add(totalIncomes.length());
			valueLengths.add(balanceValue.length());
			int maxValueLength = Collections.max(valueLengths);
			
			try {
				if (currentLine < linesPerPage - 4) {
					contentStream.newLine();
					currentLine++;
				} else {
					newPage(false);
				}
				contentStream.showText(StringUtils.repeat(" ", charsPerLine - 16 - maxValueLength) + "TOTALE USCITE = " + StringUtils.repeat(" ", maxValueLength - totalOutgoings.length()) + totalOutgoings);
				contentStream.newLine();
				contentStream.showText(StringUtils.repeat(" ", charsPerLine - 17 - maxValueLength) + "TOTALE ENTRATE = " + StringUtils.repeat(" ", maxValueLength - totalIncomes.length()) + totalIncomes);
				contentStream.newLine();
				contentStream.showText(StringUtils.repeat(" ", charsPerLine - 11 - maxValueLength) + "BILANCIO = " + StringUtils.repeat(" ", maxValueLength - balanceValue.length()) + balanceValue);
				currentLine += 2;
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
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
	
	private static void closeDocument(String totalOutgoings, String totalIncomes, String balanceValue, String path) {
		try {
			writeTotal(totalOutgoings, totalIncomes, balanceValue);
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
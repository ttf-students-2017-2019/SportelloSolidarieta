package utilities;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Formatter {

	public static String formatNumber(float number) {
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.ITALIAN);
    	numberFormat.setMinimumFractionDigits(2);
    	return numberFormat.format(number);
	}
	
	public static String formatDate(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return formatter.format(date);
	}
	
}

package utilities;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import model.Person;

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

	// Get lowerCase day of the week
	public static String getDayOfWeek(int dayOfWeek)
	{	
		String dayString = "";
		switch (dayOfWeek) {
		case Calendar.MONDAY:
			dayString = "luned�";
			break;
		case Calendar.TUESDAY:
			dayString = "marted�";
			break;
		case Calendar.WEDNESDAY:
			dayString = "mercoled�";
			break;
		case Calendar.THURSDAY:
			dayString = "gioved�";
			break;
		case Calendar.FRIDAY:
			dayString = "venerd�";
			break;
		case Calendar.SATURDAY:
			dayString = "sabato";
			break;
		case Calendar.SUNDAY:
			dayString = "domenica";
			break;
		}
		return dayString;
	}

	// Get CamelCase day of the week
	public static String getUpperCaseDayOfWeek(int dayOfWeek)
	{	
		String dayString = "";
		switch (dayOfWeek) {
		case Calendar.MONDAY:
			dayString = "Luned�";
			break;
		case Calendar.TUESDAY:
			dayString = "Marted�";
			break;
		case Calendar.WEDNESDAY:
			dayString = "Mercoled�";
			break;
		case Calendar.THURSDAY:
			dayString = "Gioved�";
			break;
		case Calendar.FRIDAY:
			dayString = "Venerd�";
			break;
		case Calendar.SATURDAY:
			dayString = "Sabato";
			break;
		case Calendar.SUNDAY:
			dayString = "Domenica";
			break;
		}
		return dayString;
	}

	public static String getAlertMessage(Person assisted, Calendar cal)
	{
		// Setting time format
		String pattern =  "HH:mm";
		DateFormat df = new SimpleDateFormat(pattern);
		Date date = cal.getTime();
		String timeAsString = df.format(date);

		return "Appuntamento assegnato a " + assisted.getName() + " " + assisted.getSurname() + " il " + 
		getDateAsString(cal)+ " (" +getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK))  + ") alle ore " + 
		timeAsString;
	}

	public static String getDateAsItalianString(Calendar cal)
	{
		return getUpperCaseDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)) + " " +getDateAsString(cal);
	}

	private static String getDateAsString (Calendar cal) 
	{
		String dayString = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String monthString = String.valueOf(cal.get(Calendar.MONTH)+1);
		String yearString = String.valueOf(cal.get(Calendar.YEAR));

		if (dayString.length()==1)
			dayString = "0"+ dayString;
		if (monthString.length()==1)
			monthString = "0" + monthString;

		return dayString + "/" + monthString + "/" + yearString;
	}

}

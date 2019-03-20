package schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Appointment;
import model.Settings;

public class DailyPlan {
	
	private static final String FREE_SLOT = "Libero";
	private static final String TAKEN_SLOT = "Assegnato";
	
	public List<Slot> dailyPlan = new ArrayList<Slot>();
	
	public DailyPlan(Date date, Settings settings) {
		
		// Setting end day and time for appointment day
		Date start = updateSettingsDate(settings.getHStart(), date);
		
		// Setting end day and time for appointment day
		Date end = updateSettingsDate(settings.getHEnd(), date);
		
		int appointmentLenght = settings.getAppointmentLength();
		
		List<Appointment> dailyAppointments = Appointment.findAppointmentsByDate(date);
		
		Date currentTime = start;
		
		while (currentTime.before(end)) {
			
			if (dailyAppointments == null) {
				
				Slot currentSlot = new Slot(currentTime, FREE_SLOT); 
				dailyPlan.add(currentSlot);
				currentTime = addMinutesToDate(currentTime, appointmentLenght);
				
			} else {
				
			}
		} 
		
		
	
	}
	
	private Date updateSettingsDate(Date settingsDate, Date dateForDay ) {
				
	// Calendar for date
	Calendar calForDay = Calendar.getInstance();
	calForDay.setTime(dateForDay);
	
	// Calendar for time
	Calendar calForTime = Calendar.getInstance();
	calForTime.setTime(settingsDate);
				
	// Updating date fields
	calForTime.set(Calendar.DAY_OF_MONTH, calForDay.get(Calendar.DAY_OF_MONTH));
	calForTime.set(Calendar.MONTH, calForDay.get(Calendar.MONTH));
	calForTime.set(Calendar.YEAR, calForDay.get(Calendar.YEAR));
	
	return calForTime.getTime();
		
	}
	
	private Date addMinutesToDate(Date date, int minutes ) {
		
		// Calendar for date
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
							
		// Updating date fields
		cal.add(Calendar.MINUTE, minutes);
		
		return cal.getTime();
			
	}
}

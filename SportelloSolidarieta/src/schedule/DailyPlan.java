package schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.Appointment;
import model.Settings;

public class DailyPlan 
{
	private List<ObservableSlot> dailyPlan = new ArrayList<ObservableSlot>();

	private List<FreeTimeSlot> dailyFreeTime = new ArrayList<FreeTimeSlot>();
	
	// Getters and Setters
	public List<ObservableSlot> getDailyPlan() {
		return dailyPlan;
	}

	public void setDailyPlan(List<ObservableSlot> dailyPlan) {
		this.dailyPlan = dailyPlan;
	}

	public List<FreeTimeSlot> getDailyFreeTimeSlot() {
		return dailyFreeTime;
	}

	public void setDailyFreeTimeSlot(List<FreeTimeSlot> dailyFreeTime) {
		this.dailyFreeTime = dailyFreeTime;
	}
	
	public DailyPlan(Date date, Settings settings) 
	{
		// Setting end day and time for appointment day
		Date start = updateSettingsDateTime(settings.getHStart(), date);
		
		// Setting end day and time for appointment day
		Date end = updateSettingsDateTime(settings.getHEnd(), date);
		
		int appointmentLenghtFromSettings = settings.getAppointmentLength();
		
		// Initialize the dailyFreeTime - All freeTime
		Calendar startTime = Calendar.getInstance();
		startTime.setTime(date);				
		Calendar endTime = Calendar.getInstance();
		endTime.setTime(date);
		endTime.add(Calendar.DATE, 1);
		dailyFreeTime.add(new FreeTimeSlot(startTime, endTime));
		
		List<Appointment> dailyAppointments = Appointment.findAppointmentsByDate(date);
		
		Date currentTime = start;
		
		// If there are no appointments at all create an all free dailyPlan
		if (dailyAppointments.isEmpty()) 
		{	
			while (currentTime.before(end)) 
			{	
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentTime);
				
				Slot currentSlot = new Slot(cal,null,settings.getAppointmentLength()); 
				ObservableSlot observableSlot = new ObservableSlot(currentSlot);
				dailyPlan.add(observableSlot);
				
				currentTime = addMinutesToDate(currentTime, appointmentLenghtFromSettings);	
			}
		} 
			else // There are appointments in the day 
		{
			
			// Generating all the slots taken by appointment already in the database 
			for (Iterator<Appointment> iterator = dailyAppointments.iterator(); iterator.hasNext();) 
			{
				Appointment currentAppointment = iterator.next();
				FreeTimeSlot currentFreeTimeSlot = checkSlot(currentAppointment.getAppointmentDateTime(), currentAppointment.getAppointmentLength());
				if ( currentFreeTimeSlot != null)
				{
					Calendar appointmentCal = Calendar.getInstance();
					appointmentCal.setTime(currentAppointment.getAppointmentDateTime());
					Slot currentSlot = new Slot(appointmentCal,currentAppointment.getPerson(),
							currentAppointment.getAppointmentLength()); 
					
					// Updating the dailyFreeTime
					upDateDailyFreeTime(currentSlot, currentFreeTimeSlot);
					
					// Adding the slot to the dailyPlan
					ObservableSlot observableSlot = new ObservableSlot(currentSlot);
					dailyPlan.add(observableSlot);
					
				}
	
			}	
			
			// Generating appointments from free time between start - end of day
			
//			while (currentTime.before(end)) 
//			{	
//				Calendar cal = Calendar.getInstance();
//				cal.setTime(currentTime);
//				
//				// Checking if at a given time is already taken
//				if ()
//				Slot currentSlot = new Slot(cal,null,settings.getAppointmentLength()); 
//				ObservableSlot observableSlot = new ObservableSlot(currentSlot);
//				dailyPlan.add(observableSlot);
//				
//				currentTime = addMinutesToDate(currentTime, appointmentLenghtFromSettings);	
//			}
			
		}
			
	}
	
	// Taking the settings time and setting the given day of appointments
	private Date updateSettingsDateTime(Date settingsDate, Date dateForDay ) 
	{	
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
	
	// Adding minutes to a dateTime
	private Date addMinutesToDate(Date date, int minutes) 
	{	
		// Calendar for date
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
							
		// Updating date fields
		cal.add(Calendar.MINUTE, minutes);
		
		return cal.getTime();			
	}
	
	// Checking if the dailyPlan has room for an appointment
	private FreeTimeSlot checkSlot(Date possibleSlot, int appointmentLength) 
	{	
		Calendar appointmentBegin = Calendar.getInstance();
		appointmentBegin.setTime(possibleSlot);
		
		Calendar appointmentEnd = Calendar.getInstance();
		appointmentEnd.setTime(possibleSlot);
		
		// Remove one second because before is a < and not <= 
		appointmentEnd.add(Calendar.MINUTE, appointmentLength);
		appointmentEnd.add(Calendar.SECOND, -1);
 		
		for (Iterator<FreeTimeSlot> iterator = dailyFreeTime.iterator(); iterator.hasNext();) 
		{
			FreeTimeSlot currentTimeSlot = iterator.next();
			
			if (appointmentBegin.after(currentTimeSlot.getStartTime()) && appointmentEnd.before(currentTimeSlot.getEndTime()) )
				return currentTimeSlot;
	
		}
		
		return null;			
	}
	
	private void upDateDailyFreeTime(Slot assignedSlot, FreeTimeSlot slotToModify) 
	{	
			System.out.println("Aggiornamento FreeTime!");
 			
	}
	
}

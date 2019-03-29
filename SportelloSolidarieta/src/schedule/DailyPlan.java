package schedule;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.Appointment;
import model.Setting;
import schedule.FreeTimeSlot;

public class DailyPlan 
{
	private int numberOfAppointments; 
	private List<ObservableSlot> dailyPlan = new ArrayList<ObservableSlot>();
	private List<FreeTimeSlot> dailyFreeTime = new ArrayList<FreeTimeSlot>();
	
	// Getters and Setters
	
	public int getNumberOfAppointments() {
		return numberOfAppointments;
	}

	public void setNumberOfAppointments(int numberOfAppointments) {
		this.numberOfAppointments = numberOfAppointments;
	}
	
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
	
	public DailyPlan(Date date, Setting settings) 
	{
		// Initializing the number of daily appointments
		numberOfAppointments = 0;
		
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
					updateDailyFreeTime(currentSlot, currentFreeTimeSlot);
					
					// Adding the slot to the dailyPlan
					ObservableSlot observableSlot = new ObservableSlot(currentSlot);
					dailyPlan.add(observableSlot);
					
					// Updating the number of appointments
					numberOfAppointments++;
				}
			}	
			
			// Generating other appointments from free time between start - end of day
			
			while (currentTime.before(end)) 
			{	
				FreeTimeSlot currentFreeTimeSlot = checkSlot(currentTime, appointmentLenghtFromSettings);
				if ( currentFreeTimeSlot != null)
				{
					Calendar appointmentTime = Calendar.getInstance();
					appointmentTime.setTime(currentTime);
					Slot currentSlot = new Slot(appointmentTime,null, appointmentLenghtFromSettings); 
					
					// Updating the dailyFreeTime
					updateDailyFreeTime(currentSlot, currentFreeTimeSlot);
					
					// Adding the slot to the dailyPlan
					ObservableSlot observableSlot = new ObservableSlot(currentSlot);
					dailyPlan.add(observableSlot);

				}
				currentTime = addMinutesToDate(currentTime, appointmentLenghtFromSettings);	
			}
			
			Calendar startOfDay = Calendar.getInstance();
			startOfDay.setTime(start);
			startOfDay.add(Calendar.SECOND, -1);
	
			Calendar endOfDay = Calendar.getInstance();
			endOfDay.setTime(end);
			endOfDay.add(Calendar.SECOND, +1);
		
			// Filling the gaps of free time with appointments with different length (not default one)
			for (Iterator iterator = dailyFreeTime.iterator(); iterator.hasNext();) 
			{
				FreeTimeSlot currentFreeSlot = (FreeTimeSlot) iterator.next();
								
				if (currentFreeSlot.getStartTime().after(startOfDay) && currentFreeSlot.getEndTime().before(endOfDay)) 
				{
					
					int appLength = (int) Duration.between(currentFreeSlot.getStartTime().toInstant(),
									  	currentFreeSlot.getEndTime().toInstant()).toMinutes();
					
					if (appLength > 0) {
						Slot currentSlot = new Slot(currentFreeSlot.getStartTime(),null, appLength); 
												
						//Adding the slot to the dailyPlan
						ObservableSlot observableSlot = new ObservableSlot(currentSlot);
						dailyPlan.add(observableSlot);
					}
				}	
			}
				
			// Sorting the dailyPlan list
			Collections.sort(dailyPlan, new Comparator<ObservableSlot>() 
			{
		        @Override
		        public int compare(ObservableSlot firstSlot, ObservableSlot secondSlot)
		        {
		            return  firstSlot.getAssociatedSlot().getDateTime().compareTo(secondSlot.getAssociatedSlot().getDateTime());
		        }
		    });	
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
		appointmentBegin.add(Calendar.SECOND, 1);
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
	
	private void updateDailyFreeTime(Slot assignedSlot, FreeTimeSlot slotToModify) 
	{	
		Calendar appointmentStart = assignedSlot.getDateTime();
		
		Calendar appointmentEnd = (Calendar) appointmentStart.clone();
		appointmentEnd.add(Calendar.MINUTE, assignedSlot.getAppointmentLength());
		
		// Control if the appointment start is the same of the the start of the slotToModify  
		if (appointmentStart.equals(slotToModify.getStartTime())) 
		{
			// Creating the new freeTimeSlot with starting time the end of the appointment  
			FreeTimeSlot newFreeTimeSlot = new FreeTimeSlot(appointmentEnd, slotToModify.getEndTime());
			
			// Replacing in the list
			dailyFreeTime.add(dailyFreeTime.indexOf(slotToModify), newFreeTimeSlot );
			dailyFreeTime.remove(slotToModify);
		}
		else if(appointmentEnd.equals(slotToModify.getEndTime())) // Appointment ends at the end of the freeTimeSlot 
		{
			// Creating the new freeTimeSlot with end time the end of the appointment  
			FreeTimeSlot newFreeTimeSlot = new FreeTimeSlot(slotToModify.getStartTime(), appointmentEnd);
			
			// Replacing in the list
			dailyFreeTime.add(dailyFreeTime.indexOf(slotToModify), newFreeTimeSlot);
			dailyFreeTime.remove(slotToModify);
		}
		else // Appointment in between
		{
			// Creating the freeTimeSlot before the taken slot
			FreeTimeSlot firstFreeTimeSlot = new FreeTimeSlot(slotToModify.getStartTime(), appointmentStart);
			
			// Creating the freeTimeSlot after the taken slot
			FreeTimeSlot secondFreeTimeSlot = new FreeTimeSlot(appointmentEnd, slotToModify.getEndTime());
			
			// Updating the dailyFreeTime
			dailyFreeTime.add(dailyFreeTime.indexOf(slotToModify), firstFreeTimeSlot);
			dailyFreeTime.add(dailyFreeTime.indexOf(slotToModify), secondFreeTimeSlot);
			dailyFreeTime.remove(slotToModify);
			
		}
	}
	
	public ObservableSlot getFirstFreeSlot()
	{
		ObservableSlot firstSlot = null;
		
		for (Iterator iterator = dailyPlan.iterator(); iterator.hasNext();) 
		{
			ObservableSlot currentSlot = (ObservableSlot) iterator.next();
			
			if (currentSlot.getAssociatedSlot().getAppointmentAssistedOwner() == null) 
			{
				firstSlot = currentSlot;
				break;
			}
		}	
		return firstSlot;
	}
}

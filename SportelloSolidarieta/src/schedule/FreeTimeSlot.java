package schedule;

import java.util.Calendar;

public class FreeTimeSlot {
	
	private Calendar startTime;
	private Calendar endTime;
	
	// Getters and Setters
	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	} 
	
	// Constructor
	public FreeTimeSlot(Calendar startTime, Calendar endTime) 
	{
		this.startTime = startTime;
		this.endTime = endTime;
	}
}

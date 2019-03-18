package schedule;

import java.time.LocalDateTime;

public class Appointment {
	
	private int assistedID;
	private LocalDateTime appointmentDateTime;
	
	public Appointment(int assistedID, LocalDateTime appointmentDateTime) {
		setAssistedID(assistedID);
		setAppointmentDateTime(appointmentDateTime);
	}
	
	// GETTERS AND SETTERS
	public LocalDateTime getAppointmentDateTime() {
		return appointmentDateTime;
	}
	
	public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
		this.appointmentDateTime = appointmentDateTime;
	}
	
	public int getAssistedID() {
		return assistedID;
	}
	
	public void setAssistedID(int assistedID) {
		 this.assistedID = assistedID;
	}

}

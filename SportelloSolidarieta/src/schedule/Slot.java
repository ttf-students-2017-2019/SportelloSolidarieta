package schedule;

import java.util.Calendar;

import model.Assisted;

public class Slot {
	
	private Calendar dateTime;
	private Assisted appointmentAssistedOwner;
	private int appointmentLength;
	
	public Calendar getDateTime() {
		return dateTime;
	}

	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

	public Assisted getAppointmentAssistedOwner() {
		return appointmentAssistedOwner;
	}

	public void setAppointmentAssistedOwner(Assisted appointmentAssistedOwner) {
		this.appointmentAssistedOwner = appointmentAssistedOwner;
	}

	public int getAppointmentLength() {
		return appointmentLength;
	}

	public void setAppointmentLength(int appointmentLength) {
		this.appointmentLength = appointmentLength;
	}

	public Slot(Calendar dateTime, Assisted appointmentAssistedOwner, int appointmentLength) {
		this.dateTime = dateTime;
		this.appointmentAssistedOwner = appointmentAssistedOwner;
		this.appointmentLength = appointmentLength;
	}
	
}

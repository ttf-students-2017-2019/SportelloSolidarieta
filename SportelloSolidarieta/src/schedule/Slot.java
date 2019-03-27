package schedule;

import java.util.Calendar;

import model.Person;

public class Slot {
	
	private Calendar dateTime;
	private Person appointmentAssistedOwner;
	private int appointmentLength;
	
	public Calendar getDateTime() {
		return dateTime;
	}

	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

	public Person getAppointmentAssistedOwner() {
		return appointmentAssistedOwner;
	}

	public void setAppointmentAssistedOwner(Person appointmentAssistedOwner) {
		this.appointmentAssistedOwner = appointmentAssistedOwner;
	}

	public int getAppointmentLength() {
		return appointmentLength;
	}

	public void setAppointmentLength(int appointmentLength) {
		this.appointmentLength = appointmentLength;
	}

	public Slot(Calendar dateTime, Person appointmentAssistedOwner, int appointmentLength) {
		this.dateTime = dateTime;
		this.appointmentAssistedOwner = appointmentAssistedOwner;
		this.appointmentLength = appointmentLength;
	}
	
}

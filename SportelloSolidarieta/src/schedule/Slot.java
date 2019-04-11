package schedule;

import java.util.Calendar;

import model.Appointment;

public class Slot {
	
	private Calendar dateTime;
	private Appointment associeatedAppointment;
	private int slotLength;
	
	public Calendar getDateTime() {
		return dateTime;
	}

	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

	public Appointment getAssocieatedAppointment() {
		return associeatedAppointment;
	}

	public void setAppointmentAssistedOwner(Appointment appointmentAssistedOwner) {
		this.associeatedAppointment = appointmentAssistedOwner;
	}

	public int getSlotLength() {
		return slotLength;
	}

	public void setSlotLength(int appointmentLength) {
		this.slotLength = appointmentLength;
	}

	public Slot(Calendar dateTime, Appointment associeatedAppointment, int slotLength) {
		this.dateTime = dateTime;
		this.associeatedAppointment = associeatedAppointment;
		this.slotLength = slotLength;
	}
	
}

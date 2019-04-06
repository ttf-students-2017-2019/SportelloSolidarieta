package schedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ObservableSlot{
	
	private static final String FREE_SLOT = "Libero";
	private static final String TAKEN_SLOT = "Assegnato";
	private static final String EMPTY_STRING = "";
	
	public SimpleStringProperty status;
	public SimpleStringProperty appointmentTimeDate;
	public SimpleStringProperty appointmentLength;
	public SimpleStringProperty assistedOwner;
	
	private Slot associatedSlot;
	
	public Slot getAssociatedSlot() {
		return associatedSlot;
	}

	public void setAssociatedSlot(Slot associatedSlot) {
		this.associatedSlot = associatedSlot;
	}

	public ObservableSlot() 
	{

	}
	
	public ObservableSlot(Slot currentSlot) 
	{	
		// Setting time format
		String pattern =  "HH:mm";
		DateFormat df = new SimpleDateFormat(pattern);
		Date date = currentSlot.getDateTime().getTime();
		String dateAsString = df.format(date);
		this.appointmentTimeDate = new SimpleStringProperty(dateAsString);
		
		String lengthString = String.valueOf(currentSlot.getAppointmentLength());
		// Adding a 0 if the appointment length is a single digit
		if(lengthString.length()==1)
			lengthString = "0" + lengthString;
		
		this.appointmentLength = new SimpleStringProperty(lengthString + " minuti");
		
		// Assigning the appointment owner only if the slot is taken
		if (currentSlot.getAppointmentAssistedOwner()==null) {
			this.status = new SimpleStringProperty(FREE_SLOT);
			this.assistedOwner = new SimpleStringProperty(EMPTY_STRING);
			
		}
		else {
			this.status = new SimpleStringProperty(TAKEN_SLOT);
			this.assistedOwner = new SimpleStringProperty(currentSlot.getAppointmentAssistedOwner().getName() + " " + currentSlot.getAppointmentAssistedOwner().getSurname());
		}

		this.associatedSlot= currentSlot;
	}
	
}

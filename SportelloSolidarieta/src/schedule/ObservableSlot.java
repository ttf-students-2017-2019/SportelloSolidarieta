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
	
	public ObservableSlot(Slot currentSlot) {
		
		//DateTimeStringConverter converter = new DateTimeStringConverter();		
		//this.appointmentTimeDate = new SimpleStringProperty(converter.toString(currentSlot.getDateTime().getTime()));
		
		// Setting time format
		String pattern =  "HH:mm";
		DateFormat df = new SimpleDateFormat(pattern);
		Date date = currentSlot.getDateTime().getTime();
		String dateAsString = df.format(date);
		this.appointmentTimeDate = new SimpleStringProperty(dateAsString);
		
		this.appointmentLength = new SimpleStringProperty(String.valueOf(currentSlot.getAppointmentLength()) + " minuti");
		
		// Assigning the appointment owner only if the slot is taken
		if (currentSlot.getAppointmentAssistedOwner()==null) {
			this.status = new SimpleStringProperty(FREE_SLOT);
			this.assistedOwner = new SimpleStringProperty(EMPTY_STRING);
			
		}
		else {
			this.status = new SimpleStringProperty(TAKEN_SLOT);
			this.assistedOwner = new SimpleStringProperty(currentSlot.getAppointmentAssistedOwner().getNome() + " " + currentSlot.getAppointmentAssistedOwner().getCognome());
		}

		this.associatedSlot= currentSlot;
	}
	
}

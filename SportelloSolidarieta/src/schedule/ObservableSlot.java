package schedule;

import javafx.beans.property.SimpleStringProperty;
import javafx.css.CssParser.ParseError.StringParsingError;
import javafx.util.converter.DateTimeStringConverter;

public class ObservableSlot {
	
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
		
		DateTimeStringConverter converter = new DateTimeStringConverter();		
		this.appointmentTimeDate = new SimpleStringProperty(converter.toString(currentSlot.getDateTime().getTime()));
		
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
